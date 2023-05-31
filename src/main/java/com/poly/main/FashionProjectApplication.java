package com.poly.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.poly.controller","com.poly.model","com.poly.repository","com.poly.config",
		"com.poly.entity","com.poly.service"})
@EnableJpaRepositories("com.poly.repository")
@EntityScan("com.poly.entity")
public class FashionProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(FashionProjectApplication.class, args);
	}

}
