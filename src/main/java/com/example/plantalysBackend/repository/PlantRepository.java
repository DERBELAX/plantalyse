package com.example.plantalysBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plantalysBackend.model.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
