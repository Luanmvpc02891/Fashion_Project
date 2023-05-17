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
	@GetMapping("/detal")
	public String detal(Model model) {
		return "detal";
	}
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
	//hi
}