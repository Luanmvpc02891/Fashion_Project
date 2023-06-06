package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
