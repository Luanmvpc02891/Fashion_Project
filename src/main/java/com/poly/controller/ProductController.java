package com.poly.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.poly.entity.Category;
import com.poly.entity.Producer;
import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.repository.CategoryRepository;
import com.poly.repository.ProducerRepo;
import com.poly.repository.ProductRepository;

import jakarta.validation.Valid;

@Controller
public class ProductController {
	@Autowired
	CategoryRepository dao1;
	@Autowired
	ProductRepository dao;
	@Autowired
	ProducerRepo producerRepo;

	@GetMapping("/shop")
	public String shop(Model model) {
		List<Product> product1 = dao.findAll();
		model.addAttribute("products", product1);
		// model.addAttribute("products", dao.findAll());
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
	public String admin(Model model, @ModelAttribute("product") Product product) {
		List<Product> product1 = dao.findAll();
		model.addAttribute("products", product1);
		List<Category> category = dao1.findAll();
		model.addAttribute("categorys", category);
		List<Producer> producer = producerRepo.findAll();
		model.addAttribute("producers", producer);
		return "admin";
	}

	@GetMapping("/admin/edit/")
	public String edit(Model model, @RequestParam("productId") int productId, Product product) {
		product = dao.findById(productId).get();
		model.addAttribute("product", product);
		List<Category> category = dao1.findAll();
		model.addAttribute("categorys", category);
		List<Product> product1 = dao.findAll();
		model.addAttribute("products", product1);
		List<Producer> producer = producerRepo.findAll();
		model.addAttribute("producers", producer);
		return "admin";
	}

	@PostMapping("/admin/create")
	public String createProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
			Model model, @RequestParam("image") MultipartFile imageFile) {
		if (bindingResult.hasErrors()) {
			// Xử lý khi dữ liệu không hợp lệ
			// return "create-product";
		}

		try {
			if (!imageFile.isEmpty()) {
				// Lưu file hình ảnh vào thư mục lưu trữ
				String fileName = imageFile.getOriginalFilename();
				String uploadDir = "C:/uploads";
				File directory = new File(uploadDir);
				if (!directory.exists()) {
				    directory.mkdirs();
				}// Đường dẫn thư mục lưu trữ hình ảnh (tương đối)
				String filePath = uploadDir + "/" + fileName;
				File dest = new File(filePath);
				imageFile.transferTo(dest);

				// Gán đường dẫn hoặc chuỗi đại diện cho trường 'image' trong đối tượng
				// 'Product'
				product.setImage(filePath);
			}

			dao.save(product);

			model.addAttribute("message", "Create success");
			model.addAttribute("products", dao.findAll());
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("error", e);
		}

		return "admin";
	}
	
	@PostMapping("/admin/delete")
	public String delete(Model model, @RequestParam("productId") int productId, Product pro) {
		if (dao.findById(pro.getProductId()).isEmpty()) {
			model.addAttribute("error", "Not UserId");

		} else {
			dao.save(pro);
			model.addAttribute("message", "Delete success ");
		}
		
		return "usermanagement"; // Trả

	}

}
