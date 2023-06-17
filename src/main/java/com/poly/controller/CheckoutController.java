package com.poly.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.poly.entity.Cart;
import com.poly.entity.CartProduct;
import com.poly.entity.InsufficientProduct;
import com.poly.entity.Inventory;
import com.poly.entity.Order;
import com.poly.entity.OrderItem;
import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.repository.CartRepository;
import com.poly.repository.Cart_ProductRepo;
import com.poly.repository.InventoryRepository;
import com.poly.repository.OrderItemRepository;
import com.poly.repository.OrderRepository;
import com.poly.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	HttpServletRequest request;

	@Autowired
	Cart_ProductRepo cartProductRepo;

	@Autowired
	UserRepository userDao;

	@Autowired
	InventoryRepository inventoryRepository;

	private Double calculateTotalPrice(List<CartProduct> cartProducts) {
		Double totalPrice = 0.0;

		for (CartProduct cartProduct : cartProducts) {
			Double productPrice = cartProduct.getProduct().getPrice().doubleValue();
			int quantity = cartProduct.getQuantity();
			totalPrice += productPrice * quantity;
		}

		return totalPrice;
	}

	@GetMapping("/checkout")
	public String checkout(Model model) {

		Integer cartId = (Integer) request.getSession().getAttribute("cartId");

		if (cartId != null) {
			Cart cart = cartRepository.findById(cartId).orElse(null);

			if (cart != null) {
				List<CartProduct> cartProducts = cart.getCartProducts();
				model.addAttribute("cartProducts", cartProducts);

				Double totalPrice = calculateTotalPrice(cartProducts);
				model.addAttribute("totalPrice", totalPrice);
			}
		}
		return "checkout";
	}

	@GetMapping("/checkout/add")
	public String checkout(Model model, HttpSession session) {
		// Lấy thông tin giỏ hàng từ session
		Integer cartId = (Integer) session.getAttribute("cartId");
		Cart cart = cartRepository.findById(cartId).orElse(null);

		// Kiểm tra giỏ hàng và người dùng
		if (cart != null && cart.getUser() != null) {
			// Kiểm tra số lượng sản phẩm trong kho trước khi đặt hàng
			List<InsufficientProduct> insufficientProducts = new ArrayList<>(); // Danh sách tên sản phẩm không đủ số
																				// lượng
			boolean sufficientQuantity = true; // Biến kiểm tra số lượng sản phẩm đủ trong kho
			for (CartProduct cartProduct : cart.getCartProducts()) {
				int quantity = cartProduct.getQuantity();
				Product product = cartProduct.getProduct();
				Inventory inventory = inventoryRepository.findByProductId(product.getProductId());
				if (inventory == null || inventory.getQuantity() < quantity) {
					sufficientQuantity = false;
					int insufficientQuantity = quantity - (inventory != null ? inventory.getQuantity() : 0);
					insufficientProducts.add(new InsufficientProduct(product.getProductName(), insufficientQuantity));

				}
			}

			if (sufficientQuantity) {
				// Tạo đối tượng Order và lưu vào bảng "orders"
				Order order = new Order();
				order.setUser(cart.getUser());
				order.setOrderDate(new Date());

				// Tính toán tổng giá của đơn hàng
				BigDecimal totalAmount = BigDecimal.ZERO;
				for (CartProduct cartProduct : cart.getCartProducts()) {
					Double productPrice = cartProduct.getProduct().getPrice().doubleValue();
					Double discount = cartProduct.getProduct().getDiscount().doubleValue();
					Double discountedPrice = productPrice - (productPrice * discount);
					int quantity = cartProduct.getQuantity();
					totalAmount = totalAmount.add(BigDecimal.valueOf(discountedPrice * quantity));
				}

				order.setTotalAmount(totalAmount);
				orderRepository.save(order);

				// Lưu các mặt hàng trong giỏ hàng vào bảng "order_items"
				List<CartProduct> cartProducts = cart.getCartProducts();
				for (CartProduct cartProduct : cartProducts) {
					OrderItem orderItem = new OrderItem();
					orderItem.setOrder(order);
					orderItem.setProduct(cartProduct.getProduct());
					orderItem.setQuantity(cartProduct.getQuantity());

					Double productPrice = cartProduct.getProduct().getPrice().doubleValue();
					Double discount = cartProduct.getProduct().getDiscount().doubleValue();
					Double discountedPrice = productPrice - (productPrice * discount);
					orderItem.setPrice(BigDecimal.valueOf(discountedPrice));

					orderItemRepository.save(orderItem);
					cartProduct.setStatus(true);
					cartProductRepo.save(cartProduct);
				}

				// Giảm số lượng sản phẩm trong kho sau khi đặt hàng thành công
				for (CartProduct cartProduct : cartProducts) {
					Product product = cartProduct.getProduct();
					int quantity = cartProduct.getQuantity();
					Inventory inventory = inventoryRepository.findByProductId(product.getProductId());
					if (inventory != null) {
						int remainingQuantity = inventory.getQuantity() - quantity;
						inventory.setQuantity(remainingQuantity);
						inventoryRepository.save(inventory);
					}
				}

				// Xóa các mặt hàng đã thanh toán khỏi cơ sở dữ liệu
				List<CartProduct> paidCartProducts = new ArrayList<>();
				for (CartProduct cartProduct : cartProducts) {
					if (cartProduct.isStatus()) {
						paidCartProducts.add(cartProduct);
					}
				}
				cartProductRepo.deleteAll(paidCartProducts);

				// Xóa các mặt hàng đã thanh toán khỏi danh sách cartProducts
				cartProducts.removeAll(paidCartProducts);
				cartRepository.save(cart);

				// Chuyển hướng người dùng đến trang xác nhận đơn hàng hoặc trang thành công
				return "redirect:/cartsuccess";
			} else {
				// Xử lý khi số lượng không đủ
				model.addAttribute("insufficientProducts", insufficientProducts);

				// Chuyển hướng người dùng đến trang lỗi hoặc trang thông báo lỗi

				return "insufficient_quantity";
			}
		} else {
			// Xử lý khi có lỗi xảy ra (ví dụ: không tìm thấy giỏ hàng hoặc người dùng)
			// ...

			// Chuyển hướng người dùng đến trang lỗi hoặc trang thông báo lỗi
			return "redirect:/shop";
		}
	}

	@GetMapping("/order")
	public String LoadOrder(Model model) {
		model.addAttribute("OrderItems", orderItemRepository.findAll());
		model.addAttribute("Orders", orderRepository.findAll());// Truyền danh sách dữ liệu vào model
		return "/admin/order"; // Trả
	}

	@GetMapping("/cartsuccess")
	public String cartsuccess(Model model) {
		return "cartsuccess"; // Trả
	}

	@GetMapping("/insufficient_quantity")
	public String insufficient_quantity(Model model) {
		return "insufficient_quantity"; // Trả
	}

	@GetMapping("/OrderItem")
	public String loadOrders(Model model) {
		// Lấy tên người dùng
		String Userid = (String) request.getSession().getAttribute("user");

		// Tìm người dùng trong cơ sở dữ liệu dựa vào tên người dùng
		User user = userDao.findByUsername(Userid);

		if (user != null) {
			// Lấy danh sách đơn hàng của người dùng
			List<Order> orders = orderRepository.findByUser(user);

			// Thêm danh sách đơn hàng vào model để hiển thị trên giao diện
			model.addAttribute("orders", orders);
		}

		return "OrderItem";
	}

	@GetMapping("/OrderItem/{orderId}")
	public String loadOrderDetails(Model model, @PathVariable("orderId") Integer orderId) {
		// Tìm đơn hàng dựa vào orderId
		Order order = orderRepository.findById(orderId).orElse(null);

		if (order != null) {
			// Lấy danh sách chi tiết đơn hàng của đơn hàng
			List<OrderItem> orderDetails = orderItemRepository.findByOrder(order);

			// Thêm đơn hàng và danh sách chi tiết vào model để hiển thị trên giao diện
			model.addAttribute("order", order);
			model.addAttribute("orderDetails", orderDetails);

		}
		String Userid = (String) request.getSession().getAttribute("user");

		// Tìm người dùng trong cơ sở dữ liệu dựa vào tên người dùng
		User user = userDao.findByUsername(Userid);

		if (user != null) {
			// Lấy danh sách đơn hàng của người dùng
			List<Order> orders = orderRepository.findByUser(user);

			// Thêm danh sách đơn hàng vào model để hiển thị trên giao diện
			model.addAttribute("orders", orders);
		}
		return "OrderItem";
	}

}
