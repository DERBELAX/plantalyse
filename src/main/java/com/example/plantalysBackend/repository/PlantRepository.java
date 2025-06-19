package com.example.plantalysBackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plantalysBackend.model.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long> {
	List<Plant> findByCategoryId(Long categoryId);
}
