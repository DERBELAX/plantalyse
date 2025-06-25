package com.example.plantalysBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plantalysBackend.model.Order;
import com.example.plantalysBackend.model.User;

public interface OrderRepository extends JpaRepository<Order, Long>{
	boolean existsByUserAndItemsPlantId(User user, Long plantId);

}
