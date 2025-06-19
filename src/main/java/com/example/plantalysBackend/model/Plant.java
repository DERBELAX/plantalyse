package com.example.plantalysBackend.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;


@Entity
@Table(name = "plant")
public class Plant {
	 
	public List<String> getImages() {
		return images;
	}


	public void setImages(List<String> images) {
		this.images = images;
	}

	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id_plante;

	    @Column(columnDefinition = "TEXT")
	    private String description;

	    @ElementCollection
	    private List<String> images;

	    private Double price;
	    private String name;
	    private Integer stock;
	    @Column(columnDefinition = "TEXT")
	    private String entretien;

	    private Integer frequenceArrosage;


	    @ManyToOne
	    @JoinColumn(name = "id_category") 
	    private Category category;

		

		


		public Plant(Long id_plante, String description, List<String> images, Double price, String name, Integer stock,
				String entretien, Integer frequenceArrosage, Category category) {
			super();
			this.id_plante = id_plante;
			this.description = description;
			this.images = images;
			this.price = price;
			this.name = name;
			this.stock = stock;
			this.entretien = entretien;
			this.frequenceArrosage = frequenceArrosage;
			this.category = category;
		}


		public Plant() {
			super();
		}
		
		
		public Long getId_plante() {
			return id_plante;
		}

		public void setId_plante(Long id_plante) {
			this.id_plante = id_plante;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getStock() {
			return stock;
		}

		public void setStock(Integer stock) {
			this.stock = stock;
		}

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}
		public String getEntretien() {
		    return entretien;
		}

		public void setEntretien(String entretien) {
		    this.entretien = entretien;
		}

		public Integer getFrequenceArrosage() {
		    return frequenceArrosage;
		}

		public void setFrequenceArrosage(Integer frequenceArrosage) {
		    this.frequenceArrosage = frequenceArrosage;
		}



}
