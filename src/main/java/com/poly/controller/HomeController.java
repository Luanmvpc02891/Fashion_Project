package com.poly.controller;

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

import com.poly.entity.User;
import com.poly.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class HomeController {

//	@GetMapping("/shop")
//	public String shop(Model model) {
//		return "shop";
//	}
//
//	@GetMapping("/detail")
//	public String detal(Model model) {
//		return "detail";
//	}

	@GetMapping("/cart")
	public String cart(Model model) {
		return "cart";
	}

	@GetMapping("/checkout")
	public String checkout(Model model) {
		return "checkout";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		return "contact";
	}

//	@GetMapping("/admin")
//	public String admin(Model model) {
//		return "admin";
//	}

	@GetMapping("/inventory")
	public String inventory(Model model) {
		return "inventory";
	}

	/* Dang ky */

	/*
	 * @Autowired private UserRepository userRepository;
	 * 
	 * @GetMapping("/dangky") public String showRegistrationForm(Model model) {
	 * model.addAttribute("user", new Users()); return "dangky"; }
	 */

//	@PostMapping("/dangky")
//	public String processRegistrationForm(@Valid @ModelAttribute("user") Users user, BindingResult result) {
//		if (result.hasErrors()) {
//			return "dangky";
//		} else {
//			userRepository.save(user);
//			return "redirect:/login";
//		}
//	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}

//	@PostMapping("/login")
//	public String processLoginForm(@Valid @ModelAttribute("user") Users user, BindingResult result, Model model,
//			@RequestParam("username") String username, @RequestParam("password") String password) {
//		user = userRepository.findByUsername(username);
//		if (user != null && user.getPassword().equals(password)) {
//			return "redirect:/index";
//		}
//
//		model.addAttribute("messages", "Đăng nhập thất bại");
//		if (result.hasErrors()) {
//			return "login";
//		}
//		return "login";
//		
//	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

}
