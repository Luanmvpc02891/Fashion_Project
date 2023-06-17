package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	List<User> findAll();
	User findByUsername(String username);
	
	User findByEmail(String email);
	
}
