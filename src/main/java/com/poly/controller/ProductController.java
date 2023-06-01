package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.Product;
import com.poly.repository.ProductRepository;

@Controller
public class ProductController {
	@Autowired
	ProductRepository dao;
	@GetMapping("/shop")
	public String shop(Model model) {
		List<Product> product1=dao.findAll();
		model.addAttribute("products", product1);
		//model.addAttribute("products", dao.findAll());
		return "shop";
	}

	@GetMapping("/shop/{productId}")
	public String detal(Model model, @PathVariable int productId) {
	    Product item = dao.findById(productId).orElse(null);
	    model.addAttribute("item", item);
	    List<Product> products = dao.findAll();
	    model.addAttribute("products", products);
	    return "detail";
	}

	@GetMapping("/admin")
	public String admin(Model model, Product product) {
		List<Product> product1=dao.findAll();
		model.addAttribute("products", product1);
		//model.addAttribute("products", dao.findAll());
		return "admin";
	}


}



