package com.poly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.Category;
import com.poly.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findAll();

	void deleteById(int id);

	@Query("SELECT o FROM Product o WHERE o.category.id LIKE ?1")
	List<Product> getByCategory(int value);

	@Query("SELECT o FROM Product o WHERE o.productName LIKE ?1")
	List<Product> findAllByNameLike(String keywords);

}
