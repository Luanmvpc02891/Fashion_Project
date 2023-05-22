package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("message", "Welcome to Spring MVC");
		return "index";
	}
	@GetMapping("/shop")
	public String shop(Model model) {	
		return "shop";
	}
	@GetMapping("/detail")
	public String detal(Model model) {
		return "detail";
	}
	@GetMapping("/cart")
	public String cart(Model model) {
		return "cart";
	}
	@GetMapping("/checkout")
	public String checkout(Model model) {
		return "checkout";
	}
	@GetMapping("/Login")
	public String Login(Model model) {
		return "Login";
	}
	@GetMapping("/dangky")
	public String dangky(Model model) {
		return "dangky";
	}
	@GetMapping("/contact")
	public String contact(Model model) {
		return "contact";
	}
	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}
	
}