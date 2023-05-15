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
}