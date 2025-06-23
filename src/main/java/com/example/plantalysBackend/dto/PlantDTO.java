package com.example.plantalysBackend.dto;

public class PlantDTO {
    private Long id_plante;
    private String name;

    public PlantDTO(Long id_plante, String name) {
        this.id_plante = id_plante;
        this.name = name;
    }

    public Long getId_plante() {
        return id_plante;
    }

    public String getName() {
        return name;
    }
}
