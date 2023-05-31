package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

}
