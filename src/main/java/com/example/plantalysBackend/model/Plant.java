package com.example.plantalysBackend.model;

import jakarta.persistence.Column;
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
	 

	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id_plante;

	    @Column(columnDefinition = "TEXT")
	    private String description;

	    private String image;
	    private Double price;
	    private String name;
	    private Integer stock;

	    @ManyToOne
	    @JoinColumn(name = "id_category") 
	    private Category category;

		public Plant(Long id_plante, String description, String image, 
				Double price, String name, 
				Integer stock, Category category) {
			super();
			this.id_plante = id_plante;
			this.description = description;
			this.image = image;
			this.price = price;
			this.name = name;
			this.stock = stock;
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

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
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


}
