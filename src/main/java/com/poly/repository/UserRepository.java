package com.poly.repository;

import org.springframework.data.repository.CrudRepository;

import com.poly.model.Users;

public interface UserRepository extends CrudRepository<Users, String> {
    Users findByUsername(String username);
}

