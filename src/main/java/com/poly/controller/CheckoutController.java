package com.poly.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.entity.Cart;
import com.poly.entity.CartProduct;
import com.poly.entity.Order;
import com.poly.entity.OrderItem;
import com.poly.repository.CartRepository;
import com.poly.repository.Cart_ProductRepo;
import com.poly.repository.OrderItemRepository;
import com.poly.repository.OrderRepository;

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
			// Tạo đối tượng Order và lưu vào bảng "orders"
			Order order = new Order();
			order.setUser(cart.getUser());
			order.setOrderDate(new Date());
			order.setTotalAmount(BigDecimal.valueOf(calculateTotalPrice(cart.getCartProducts())));
			orderRepository.save(order);

			// Lưu các mặt hàng trong giỏ hàng vào bảng "order_items"
			List<CartProduct> cartProducts = cart.getCartProducts();
			for (CartProduct cartProduct : cartProducts) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder(order);
				orderItem.setProduct(cartProduct.getProduct());
				orderItem.setQuantity(cartProduct.getQuantity());
				orderItem.setPrice(cartProduct.getProduct().getPrice());
				orderItemRepository.save(orderItem);
				cartProduct.setStatus(true);
				cartProductRepo.save(cartProduct);
			}

			// Xóa các mặt hàng trong giỏ hàng sau khi đã thanh toán
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


			//.removeAttribute("cartId");

			// Thực hiện các công việc khác sau khi thanh toán thành công (ví dụ: gửi email
			// xác nhận, cập nhật kho hàng, v.v.)
			// ...

			// Chuyển hướng người dùng đến trang xác nhận đơn hàng hoặc trang thành công
			return "redirect:/checkout";
		} else {
			// Xử lý khi có lỗi xảy ra (ví dụ: không tìm thấy giỏ hàng hoặc người dùng)
			// ...

			// Chuyển hướng người dùng đến trang lỗi hoặc trang thông báo lỗi
			return "redirect:/shop";
		}
	}

}
