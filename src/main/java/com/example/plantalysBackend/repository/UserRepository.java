package com.example.plantalysBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plantalysBackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); 
}


