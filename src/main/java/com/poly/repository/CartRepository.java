package com.poly.repository;

import java.util.HashMap;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Cart;
import com.poly.entity.cartDto;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}
