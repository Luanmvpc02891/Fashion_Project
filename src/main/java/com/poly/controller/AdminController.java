package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.User;
import com.poly.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class AdminController {
	@Autowired
	UserRepository RepoUser;

	@GetMapping("/usermanagement")
	public String usermanagement(Model model, @ModelAttribute("user") User user) {
		model.addAttribute("users", RepoUser.findAll()); // Truyền danh sách dữ liệu vào model
		return "usermanagement"; // Trả
	}

	@GetMapping("/usermanagement/edit/")
	public String usermanagement_edit(Model model, @RequestParam("userId") int userId, User user) {
		user = RepoUser.findById(userId).get();
		model.addAttribute("user", user);
		model.addAttribute("users", RepoUser.findAll()); // Truyền danh sách dữ liệu vào model
		return "usermanagement"; // Trả
	}

	@PostMapping("/usermanagement/create")
	public String usermanagement_create(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "usermanagement"; // Trả
		} else {
			user.setIsative(true);
			RepoUser.save(user);
			model.addAttribute("message", "Create success ");
			model.addAttribute("users", RepoUser.findAll()); // Truyền danh sách dữ liệu vào model
			return "usermanagement";
		}
	}

	@PostMapping("/usermanagement/update")
	public String usermanagement_update(@ModelAttribute("user") User user, BindingResult result, Model model) {
		user.setIsative(true);
		RepoUser.save(user);
		model.addAttribute("message", "Update success ");
		model.addAttribute("users", RepoUser.findAll()); // Truyền danh sách dữ liệu vào model
		return "usermanagement"; // Trả
	}

	@PostMapping("/usermanagement/delete")
	public String usermanagement_delete(Model model, @RequestParam("userId") int userId, User user) {
		if (RepoUser.findById(user.getUserId()).isEmpty()) {
			model.addAttribute("error", "Not UserId");

		} else {
			user.setIsative(false);
			RepoUser.save(user);
			model.addAttribute("message", "Delete success ");
		}
		// RepoUser.deleteById(userId);
		usermanagement_reset(model);
		model.addAttribute("users", RepoUser.findAll()); // Truyền danh sách dữ liệu vào model
		return "usermanagement"; // Trả

	}

	@PostMapping("/usermanagement/reset")
	public String usermanagement_reset(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("users", RepoUser.findAll()); // Truyền danh sách dữ liệu vào model
		return "usermanagement"; // Trả
	}
}
