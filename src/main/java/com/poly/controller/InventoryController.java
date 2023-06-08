package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.Inventory;

import com.poly.entity.Product;

import com.poly.repository.InventoryRepository;
import com.poly.repository.ProductRepository;

@Controller
public class InventoryController {
	@Autowired
	InventoryRepository RepoInventory;
	@Autowired
	ProductRepository RepoProduct;

	@GetMapping("/inventory")
	public String inventory(Model model, @ModelAttribute("productt") Product productt) {
		model.addAttribute("inventorys", RepoInventory.findAll());
		List<Product> product1 = RepoProduct.findAll();
		model.addAttribute("products", product1);
		return "inventory";
	}

	@GetMapping("/inventory/edit/")
	public String usermanagement_edit(Model model, @RequestParam("inventoryId") int inventoryId) {
		Inventory inventory = RepoInventory.findById(inventoryId).get();
		model.addAttribute("inventorys", RepoInventory.findAll());
		List<Product> product1 = RepoProduct.findAll();
		model.addAttribute("inventory", inventory);
		model.addAttribute("products", product1);
		return "inventory";
	}

	@PostMapping("/inventory/delete")
	public String delete(@RequestParam("inventoryId") int inventoryId, Model model) {
		RepoInventory.deleteById(inventoryId);
		model.addAttribute("message", "Delete Success!");
		// Xử lý sau khi xóa sản phẩm
		return "redirect:/inventory";
	}

	@PostMapping("/inventory/reset")
	public String reset(Model model) {
		Inventory inventory = new Inventory();
		model.addAttribute("inventory", inventory);
		List<Inventory> inventorys = RepoInventory.findAll();
		List<Product> Products = RepoProduct.findAll();
		model.addAttribute("inventorys", inventorys);
		model.addAttribute("Products", Products);

		return "inventory"; // Trả
	}

}
