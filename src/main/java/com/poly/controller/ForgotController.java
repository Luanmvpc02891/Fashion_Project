package com.poly.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.User;
import com.poly.repository.UserRepository;
import com.poly.service.MailInfo;
import com.poly.service.MailerService;

import jakarta.mail.MessagingException;

@Controller
public class ForgotController {
	
	@Autowired
	UserRepository dao;
	
	@Autowired
	MailerService impl;
	public String generateRandomString() {
        int length = 10; // Độ dài của chuỗi ký tự ngẫu nhiên
        String characters = "ABCDEF123456";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
	
	@GetMapping("/account/quenMK")
	public String QuenMK(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "quenMK";
	}
	
	@PostMapping("/account/quenMK")
	public String post_forgot(Model model, @ModelAttribute("user") User user,@RequestParam("email")String recipient) {
		
		boolean check = false;
		List<User> list = dao.findAll();
		for (User nguoiDung : list) {
			if(nguoiDung.getEmail().equals(user.getEmail())) {
				check = true;
			}
		}
		
		if(check == true) {
			String newPassword = generateRandomString();
			User customer = dao.findByEmail(user.getEmail());
			customer.setPassword(newPassword);
			dao.save(customer);
			
			String subject = "Đổi mật khẩu";
			String message = newPassword;
			
			MailInfo mail = new MailInfo(recipient, subject, message);
			try {
				impl.send(mail);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "login";
		}else {
		return "quenMK";
	}}
}
