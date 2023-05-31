package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findAll();
}
