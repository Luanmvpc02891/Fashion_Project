package com.poly.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.Cart;
import com.poly.entity.CartProduct;
import com.poly.entity.Category;
import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.repository.CartRepository;
import com.poly.repository.CategoryRepository;
import com.poly.repository.ProductRepository;
import com.poly.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	UserRepository dao;

	@Autowired
	ProductRepository dao1;

	@Autowired
	CategoryRepository dao2;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	HttpServletRequest request;

	@GetMapping("/index")
	public String index(Model model) {
		List<Product> product = dao1.findAll();
		List<Category> category = dao2.findAll();
		model.addAttribute("products", product);
		model.addAttribute("categorys", category);
		return "index";
	}



	@GetMapping("/contact")
	public String contact(Model model) {
		return "contact";
	}

	@GetMapping("/inventory")
	public String inventory(Model model) {
		return "inventory";
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}

	@PostMapping("/login")
	public String processLoginForm(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,
			@RequestParam("username") String username, @RequestParam("password") String password) {
		user = dao.findByUsername(username);

		if (user != null && user.getPassword().equals(password)) {
			if (user.isRole()) {
				return "redirect:/usermanagement";
			} else {
				// Kiểm tra xem người dùng đã có giỏ hàng trước đó hay chưa
				Cart cart = null;
				if (user.getCarts().isEmpty()) {
					// Nếu chưa có giỏ hàng, tạo giỏ hàng mới
					cart = new Cart();
					cart.setUser(user);
					cart.setCartDate(new Date());
					// Lưu đối tượng Cart vào cơ sở dữ liệu bằng JpaRepository
					cartRepository.save(cart);
				} else {
					// Nếu đã có giỏ hàng, sử dụng lại giỏ hàng đó
					cart = user.getCarts().get(0);
				}

				// Lưu cartId vào session
				request.getSession().setAttribute("cartId", cart.getCartId());

				return "redirect:/index";
			}
		}

		model.addAttribute("messages", "Đăng nhập thất bại");
		if (result.hasErrors()) {
			return "login";
		}
		return "login";
	}


	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// Xóa các thuộc tính trong session
		session.invalidate();

		return "redirect:/login"; // Chuyển hướng người dùng đến trang đăng nhập sau khi logout
	}

}
