package com.example.plantalysBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plantalysBackend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
