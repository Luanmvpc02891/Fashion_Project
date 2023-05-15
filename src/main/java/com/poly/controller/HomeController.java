package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // chi ra day la controller
public class HomeController {
	@GetMapping("/index") /*đánh dấu rằng phương thức được định nghĩa trong lớp này sẽ được sử dụng để
							 * xử lý các request HTTP GET đến một đường dẫn cụ thể.
							 */
	public String index (Model model) {
		model.addAttribute("message", "Welcome to Spring MVC");////lưu trữ các giá trị trong đối tượng request để chuyển dữ liệu đến index.jsp.
		return "index";// trả về trang index.jsp
	}
}