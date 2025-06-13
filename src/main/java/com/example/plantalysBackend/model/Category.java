package com.example.plantalysBackend.model;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
	  
	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id_category;
	    private String name;
	    @OneToMany(mappedBy = "category")
	    private List<Plant> plants;
		public Category(Long id_category, String name, List<Plant> plants) {
			super();
			this.id_category = id_category;
			this.name = name;
			this.plants = plants;
		}
		public Category() {
			super();
		}
	    
		public Long getId_category() {
			return id_category;
		}
		public void setId_category(Long id_category) {
			this.id_category = id_category;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Plant> getPlants() {
			return plants;
		}
		public void setPlants(List<Plant> plants) {
			this.plants = plants;
		}
}
