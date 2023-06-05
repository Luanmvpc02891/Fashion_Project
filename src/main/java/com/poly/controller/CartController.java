package com.poly.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.entity.cartDto;
import com.poly.repository.ProductRepository;
import com.poly.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	@Autowired
	ProductRepository dao;

	@Autowired
	UserRepository userDao;

	@PostMapping("/login")
	public String processLoginForm(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession session) {
		User authenticatedUser = userDao.findByUsername(username);

		if (authenticatedUser != null && authenticatedUser.getPassword().equals(password)) {
			Long userId = (long) authenticatedUser.getUserId();

			// Tạo giỏ hàng riêng cho người dùng và lưu vào session
			Map<Long, cartDto> cart = new HashMap<>();
			session.setAttribute("cart_" + userId, cart);

			// Xóa giỏ hàng cũ (nếu có) của người dùng đăng nhập trước đó
			session.removeAttribute("cart");

			// Lưu thông tin người dùng vào session
			session.setAttribute("currentUser", authenticatedUser);

			// Chuyển hướng đến trang sau khi đăng nhập thành công
			return "redirect:/index";
		}

		// Xử lý khi đăng nhập thất bại
		// ...

		return "login";
	}

	@RequestMapping("/cart")
	public String cart(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser == null) {
			// Chưa đăng nhập, chuyển hướng đến trang đăng nhập
			return "redirect:/login";
		}

		Long userId = (long) currentUser.getUserId();
		Map<Long, cartDto> cart = (Map<Long, cartDto>) session.getAttribute("cart_" + userId);
		if (cart == null) {
			cart = new HashMap<Long, cartDto>();
			session.setAttribute("cart_" + userId, cart);
		}

		model.addAttribute("userId", userId);

		List<cartDto> cartItems = new ArrayList<>(cart.values());
		model.addAttribute("cartItems", cartItems);

		return "cart";
	}

	@RequestMapping("/cart/{productId}")
	public String addToCart(@PathVariable("productId") long productId, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser == null) {
			// Chưa đăng nhập, chuyển hướng đến trang đăng nhập
			return "redirect:/login";
		}

		Long userId = (long) currentUser.getUserId();
		Map<Long, cartDto> cart = (Map<Long, cartDto>) session.getAttribute("cart_" + userId);
		if (cart == null) {
			cart = new HashMap<Long, cartDto>();
			session.setAttribute("cart_" + userId, cart);
		}

		if (cart.containsKey(productId)) {
			cartDto item = cart.get(productId);
			item.setQuantity(item.getQuantity() + 1);
		} else {
			Product product = dao.findById((int) productId).orElse(null);
			if (product != null) {
				cartDto item = new cartDto();
				item.setdProduct(product);
				item.setQuantity(1);
				cart.put(productId, item);
			}
		}

		return "redirect:/cart";
	}

	@RequestMapping("/cart/remove/{productId}")
	public String removeFromCart(@PathVariable("productId") long productId, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser == null) {
			// Chưa đăng nhập, chuyển hướng đến trang đăng nhập
			return "redirect:/login";
		}

		Long userId = (long) currentUser.getUserId();
		Map<Long, cartDto> cart = (Map<Long, cartDto>) session.getAttribute("cart_" + userId);
		if (cart != null) {
			cart.remove(productId);
		}

		return "redirect:/cart";
	}

	@RequestMapping("/cart/update/{productId}")
	public String updateCart(@PathVariable("productId") long productId, @RequestParam("quantity") int quantity,
			HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser == null) {
			// Chưa đăng nhập, chuyển hướng đến trang đăng nhập
			return "redirect:/login";
		}

		Long userId = (long) currentUser.getUserId();
		Map<Long, cartDto> cart = (Map<Long, cartDto>) session.getAttribute("cart_" + userId);
		if (cart != null) {
			if (cart.containsKey(productId)) {
				cartDto item = cart.get(productId);
				item.setQuantity(quantity);
			}
		}

		return "redirect:/cart";
	}

	@RequestMapping("/cart/clear")
	public String clearCart(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser == null) {
			// Chưa đăng nhập, chuyển hướng đến trang đăng nhập
			return "redirect:/login";
		}

		Long userId = (long) currentUser.getUserId();
		session.removeAttribute("cart_" + userId);

		return "redirect:/cart";
	}

	@RequestMapping("/cart/total")
	public String cartTotal(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser == null) {
			// Chưa đăng nhập, chuyển hướng đến trang đăng nhập
			return "redirect:/login";
		}

		Long userId = (long) currentUser.getUserId();
		Map<Long, cartDto> cart = (Map<Long, cartDto>) session.getAttribute("cart_" + userId);
		BigDecimal total = BigDecimal.ZERO;

		if (cart != null) {
			for (cartDto item : cart.values()) {
				BigDecimal price = item.getdProduct().getPrice();
				int quantity = item.getQuantity();
				total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
			}
		}

		model.addAttribute("total", total);

		return "cart";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // Xóa tất cả dữ liệu trên session
		return "redirect:/login";
	}
}
