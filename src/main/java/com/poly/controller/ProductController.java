package com.poly.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.poly.entity.Category;
import com.poly.entity.Producer;
import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.repository.CategoryRepository;
import com.poly.repository.ProducerRepo;
import com.poly.repository.ProductRepository;
import com.poly.service.SessionService;

import jakarta.validation.Valid;

@Controller
public class ProductController {
	@Autowired
	CategoryRepository dao1;
	@Autowired
	ProductRepository dao;
	@Autowired
	ProducerRepo producerRepo;
	@Autowired
	SessionService session;

//	@GetMapping("/shop")
//	public String shop(Model model) {
//		List<Product> product1 = dao.findAll();
//		model.addAttribute("products", product1);
//		// model.addAttribute("products", dao.findAll());
//		return "shop";
//	}
	
	@GetMapping("/shop")
	public String shop(Model model, @RequestParam("value") int value) {
		List<Product> product1 = dao.getByCategory(value);
		model.addAttribute("products", product1);
		return "shop";
	}

	@GetMapping("/shop/seach")
	public String caterogy(Model model) {
		List<Product> product1 = dao.findAll();
		model.addAttribute("products", product1);
		// model.addAttribute("products", dao.findAll())
		return "shop";
	}

	@RequestMapping("/shop/seach2")
	public String searchAndPage(Model model, @RequestParam("keywords") String kw,Product product) {
		if(kw.equals(null)||!kw.equals(product.getProductName())) {
			List<Product> product1 = dao.findAll();
			model.addAttribute("products", product1);
		}else {
			List<Product> product1 = dao.findAllByNameLike("%" + kw + "%");
			model.addAttribute("products", product1);
		}
		return "shop";	
	}

	@GetMapping("/shop/page1")
	public String paginate(Model model, @RequestParam("p") Optional<Integer> p) {
	Pageable pageable = PageRequest.of(p.orElse(0), 5);
	Page<Product> page = dao.findAll(pageable);
	model.addAttribute("products", page);
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
			Model model, @RequestParam("image") MultipartFile imageFile, @RequestParam("category_id") int categoryId,
			@RequestParam("producer_id") int producerId) {
		
			
		try {
			
			
			if (!imageFile.isEmpty()) {
				// Lưu file hình ảnh vào thư mục lưu trữ
				String fileName = imageFile.getOriginalFilename();
				String uploadDir = "C:/uploads";
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				} // Đường dẫn thư mục lưu trữ hình ảnh (tương đối)

				String filePath = uploadDir + "/" + fileName;
				File dest = new File(filePath);
				imageFile.transferTo(dest);

				// Gán đường dẫn hoặc chuỗi đại diện cho trường 'image' trong đối tượng
				// 'Product'
				product.setImage(filePath);
			}
			Category category1 = dao1.findById(categoryId).get();
			Producer producer1 = producerRepo.findById(producerId).get();

			product.setCategory(category1);
			product.setProducer(producer1);
			if (bindingResult.getErrorCount()>1) {
				
				List<Product> product1 = dao.findAll();
				model.addAttribute("products", product1);
				List<Category> category = dao1.findAll();
				model.addAttribute("categorys", category);
				List<Producer> producer = producerRepo.findAll();
				model.addAttribute("producers", producer);
				return "admin";
			} 
			dao.save(product);
			
			model.addAttribute("message", "Create success");
			model.addAttribute("products", dao.findAll());
			List<Category> category = dao1.findAll();
			model.addAttribute("categorys", category);
			List<Producer> producer = producerRepo.findAll();
			model.addAttribute("producers", producer);
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("error", e);
		}
		
		
		return "redirect:/admin";
	}

	@PostMapping("/admin/update")
	public String updateProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
			Model model, @RequestParam("image") MultipartFile imageFile, @RequestParam("category_id") int categoryId,
			@RequestParam("producer_id") int producerId) {
		if (bindingResult.hasErrors()) {
			// Xử lý khi dữ liệu không hợp lệ
			// return "update-product";
		}

		try {
			Product existingProduct = dao.findById(product.getProductId()).orElse(null);
			if (existingProduct != null) {
				// Cập nhật các trường thông tin của sản phẩm
				existingProduct.setProductName(product.getProductName());
				existingProduct.setPrice(product.getPrice());
				existingProduct.setQuantity(product.getQuantity());
				existingProduct.setDiscount(product.getDiscount());

				// Cập nhật danh mục và nhà sản xuất
				Category category = dao1.findById(categoryId).orElse(null);
				Producer producer = producerRepo.findById(producerId).orElse(null);
				existingProduct.setCategory(category);
				existingProduct.setProducer(producer);

				if (!imageFile.isEmpty()) {
					// Xử lý file hình ảnh nếu có thay đổi
					String fileName = imageFile.getOriginalFilename();
					String uploadDir = "C:/uploads";
					File directory = new File(uploadDir);
					if (!directory.exists()) {
						directory.mkdirs();
					}
					String filePath = uploadDir + "/" + fileName;
					File dest = new File(filePath);
					imageFile.transferTo(dest);

					// Cập nhật đường dẫn hình ảnh
					existingProduct.setImage(filePath);
				}

				dao.save(existingProduct);

				model.addAttribute("message", "Update success");
			} else {
				model.addAttribute("error", "Product not found");
			}

			// Load lại danh sách sản phẩm và các thông tin khác để hiển thị trên giao diện
			List<Product> products = dao.findAll();
			List<Category> categories = dao1.findAll();
			List<Producer> producers = producerRepo.findAll();
			model.addAttribute("products", products);
			model.addAttribute("categorys", categories);
			model.addAttribute("producers", producers);
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("error", e);
		}

		return "admin";
	}

	@PostMapping("/admin/update")
	public String updateProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
			Model model, @RequestParam("image") MultipartFile imageFile, @RequestParam("category_id") int categoryId,
			@RequestParam("producer_id") int producerId) {
		if (bindingResult.hasErrors()) {
			// Xử lý khi dữ liệu không hợp lệ
			// return "update-product";
		}

		try {
			Product existingProduct = dao.findById(product.getProductId()).orElse(null);
			if (existingProduct != null) {
				// Cập nhật các trường thông tin của sản phẩm
				existingProduct.setProductName(product.getProductName());
				existingProduct.setPrice(product.getPrice());
				existingProduct.setQuantity(product.getQuantity());
				existingProduct.setDiscount(product.getDiscount());

	            // Cập nhật danh mục và nhà sản xuất
	            Category category = dao1.findById(categoryId).orElse(null);
	            Producer producer = producerRepo.findById(producerId).orElse(null);
	            existingProduct.setCategory(category);
	            existingProduct.setProducer(producer);
	            existingProduct.setActive(true);
	            if (!imageFile.isEmpty()) {
	                // Xử lý file hình ảnh nếu có thay đổi
	                String fileName = imageFile.getOriginalFilename();
	                String uploadDir = "C:/uploads";
	                File directory = new File(uploadDir);
	                if (!directory.exists()) {
	                    directory.mkdirs();
	                }
	                String filePath = uploadDir + "/" + fileName;
	                File dest = new File(filePath);
	                imageFile.transferTo(dest);

					// Cập nhật đường dẫn hình ảnh
					existingProduct.setImage(filePath);
				}

				dao.save(existingProduct);

				model.addAttribute("message", "Update success");
			} else {
				model.addAttribute("error", "Product not found");
			}

			// Load lại danh sách sản phẩm và các thông tin khác để hiển thị trên giao diện
			List<Product> products = dao.findAll();
			List<Category> categories = dao1.findAll();
			List<Producer> producers = producerRepo.findAll();
			model.addAttribute("products", products);
			model.addAttribute("categorys", categories);
			model.addAttribute("producers", producers);
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("error", e);
		}

		return "admin";
	}

	
	@PostMapping("/admin/delete")
	public String deleteProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
	        Model model, @RequestParam("image") MultipartFile imageFile, @RequestParam("category_id") int categoryId,
	        @RequestParam("producer_id") int producerId) {
	    if (bindingResult.hasErrors()) {
	        // Xử lý khi dữ liệu không hợp lệ
	        // return "update-product";
	    }

	    try {
	        Product existingProduct = dao.findById(product.getProductId()).orElse(null);
	        if (existingProduct != null) {
	            // Cập nhật các trường thông tin của sản phẩm
	            existingProduct.setProductName(product.getProductName());
	            existingProduct.setPrice(product.getPrice());
	            existingProduct.setQuantity(product.getQuantity());
	            existingProduct.setDiscount(product.getDiscount());

	            // Cập nhật danh mục và nhà sản xuất
	            Category category = dao1.findById(categoryId).orElse(null);
	            Producer producer = producerRepo.findById(producerId).orElse(null);
	            existingProduct.setCategory(category);
	            existingProduct.setProducer(producer);
	            existingProduct.setActive(false);
	            if (!imageFile.isEmpty()) {
	                // Xử lý file hình ảnh nếu có thay đổi
	                String fileName = imageFile.getOriginalFilename();
	                String uploadDir = "C:/uploads";
	                File directory = new File(uploadDir);
	                if (!directory.exists()) {
	                    directory.mkdirs();
	                }
	                String filePath = uploadDir + "/" + fileName;
	                File dest = new File(filePath);
	                imageFile.transferTo(dest);

	                // Cập nhật đường dẫn hình ảnh
	                existingProduct.setImage(filePath);
	            }

	            dao.save(existingProduct);

	            model.addAttribute("message", "delete success");
	        } else {
	            model.addAttribute("error", "Product not found");
	        }

	        // Load lại danh sách sản phẩm và các thông tin khác để hiển thị trên giao diện
	        List<Product> products = dao.findAll();
	        List<Category> categories = dao1.findAll();
	        List<Producer> producers = producerRepo.findAll();
	        model.addAttribute("products", products);
	        model.addAttribute("categorys", categories);
	        model.addAttribute("producers", producers);
	    } catch (IOException e) {
	        e.printStackTrace();
	        model.addAttribute("error", e);
	    }

	    return "admin";
	}

//	@PostMapping("/admin/delete")
//	public String delete(@RequestParam("productId") int productId,Model model) {
//		dao.deleteById(productId);
//		model.addAttribute("message", "Delete Success!");
//		// Xử lý sau khi xóa sản phẩm
//		return "redirect:/admin";
//	}
    
	@PostMapping("/admin/reset")
	public String reset(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		List<Product> products = dao.findAll();
		List<Category> categories = dao1.findAll();
		List<Producer> producers = producerRepo.findAll();
		model.addAttribute("products", products);
		model.addAttribute("categorys", categories);
		model.addAttribute("producers", producers);

		return "admin"; // Trả
	}

}
