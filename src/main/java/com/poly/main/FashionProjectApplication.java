package com.poly.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.poly.controller"})
public class FashionProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(FashionProjectApplication.class, args);
	}

}
