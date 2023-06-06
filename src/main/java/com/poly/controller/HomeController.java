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
		List<Product> product=dao1.findAll();
		List<Category> category=dao2.findAll();
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

//	@GetMapping("/inventory")
//	public String inventory(Model model) {
//		return "inventory";
//	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
	    model.addAttribute("user", new User());
	    return "login";
	}


}
