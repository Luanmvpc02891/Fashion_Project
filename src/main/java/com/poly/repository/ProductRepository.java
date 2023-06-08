package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findAll();

	void deleteById(int id);

	@Query("SELECT p.category.categoryName, COUNT(p) FROM Product p GROUP BY p.category.categoryName")
	List<Object[]> countProductsByCategory();

	@Query("SELECT p.producer.producerName, COUNT(p) FROM Product p GROUP BY p.producer.producerName")
	List<Object[]> countProductsByProducer();

}
