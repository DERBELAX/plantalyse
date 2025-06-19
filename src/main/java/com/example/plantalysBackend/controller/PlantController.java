package com.example.plantalysBackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.plantalysBackend.model.Plant;
import com.example.plantalysBackend.service.PlantService;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "*")
public class PlantController {
	   @Autowired
	    private PlantService plantService;

	    @GetMapping
	    public List<Plant> getAllPlants() {
	        return plantService.getAllPlants();
	    }

	    @GetMapping("/{id}")
	    public Optional<Plant> getPlantById(@PathVariable Long id) {
	        return plantService.getPlantById(id);
	    }

	    @PostMapping
	    public Plant createPlant(@RequestBody Plant plant) {
	        return plantService.createPlant(plant);
	    }

	    @PutMapping("/{id}")
	    public Plant updatePlant(@PathVariable Long id, @RequestBody Plant plant) {
	        return plantService.updatePlant(id, plant);
	    }

	    @DeleteMapping("/{id}")
	    public void deletePlant(@PathVariable Long id) {
	        plantService.deletePlant(id);
	    }
	    
	    @GetMapping("/by-category/{id}")
	    public List<Plant> getPlantsByCategory(@PathVariable Long id) {
	        return plantService.getPlantsByCategoryId(id);
	    }


}
