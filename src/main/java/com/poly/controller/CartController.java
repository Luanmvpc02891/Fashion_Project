package com.poly.controller;

import org.springframework.stereotype.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.entity.Product;
import com.poly.repository.ProductRepository;

@Controller
public class CartController {
	@Autowired
	ProductRepository dao;

	@GetMapping("/cart")
	public String cart(Model model) {
		return "cart";
	}

	@RequestMapping("/cart/{productId}")
	public String edit(Model model, @PathVariable("productId") Integer productId) {
		Product item = dao.findById(productId).get();
		model.addAttribute("item", item);
		List<Product> items = dao.findAll();
		return "cart";
	}
}
