package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import com.poly.entity.Category;
import com.poly.entity.Product;
import com.poly.entity.User;

import com.poly.repository.CategoryRepository;
import com.poly.repository.ProductRepository;
import com.poly.repository.UserRepository;

@Controller
public class HomeController {
	@Autowired
	UserRepository dao;

	@Autowired
	ProductRepository dao1;

	@Autowired
	CategoryRepository dao2;

	@GetMapping("/index")
	public String index(Model model) {
		List<Product> product = dao1.findAll();
		List<Category> category = dao2.findAll();
		model.addAttribute("products", product);
		model.addAttribute("categorys", category);
		return "index";
	}

	@GetMapping("/checkout")
	public String checkout(Model model) {
		return "checkout";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		return "contact";
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}

	// @PostMapping("/login")
	// public String processLoginForm(@Valid @ModelAttribute("user") User user,
	// BindingResult result, Model model,
	// @RequestParam("username") String username, @RequestParam("password") String
	// password, HttpSession session) {
	//
	// User authenticatedUser = dao.findByUsername(username);
	//
	// if (authenticatedUser != null &&
	// authenticatedUser.getPassword().equals(password)) {
	// // Tạo giỏ hàng riêng cho người dùng và lưu vào session
	// Map<Long, cartDto> cart = (Map<Long, cartDto>) session.getAttribute("cart_" +
	// authenticatedUser.getUserId());
	// if (cart == null) {
	// cart = new HashMap<>();
	// session.setAttribute("cart_" + authenticatedUser.getUserId(), cart);
	// }
	//
	// if (authenticatedUser.isRole()) {
	// session.setAttribute("currentUser", authenticatedUser);
	// return "redirect:/usermanagement";
	// } else {
	// session.setAttribute("currentUser", authenticatedUser);
	// return "redirect:/index";
	// }
	// }
	//
	// model.addAttribute("messages", "Đăng nhập thất bại");
	// if (result.hasErrors()) {
	// return "login";
	// }
	//
	// return "login";
	// }

}
