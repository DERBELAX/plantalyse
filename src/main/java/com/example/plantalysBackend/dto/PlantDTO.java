package com.example.plantalysBackend.dto;

import java.util.List;

public class PlantDTO {
   

	private Long id_plante;
    private String name;
    private String mainImage;

    public PlantDTO(Long id_plante, String name, List<String> images) {
        this.id_plante = id_plante;
        this.name = name;
        this.mainImage = (images != null && !images.isEmpty()) ? images.get(0) : null;
    }

    public Long getId_plante() {
        return id_plante;
    }

    public String getName() {
        return name;
    }

    public String getMainImage() {
        return mainImage;
    }
    public void setId_plante(Long id_plante) {
		this.id_plante = id_plante;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
    
}
