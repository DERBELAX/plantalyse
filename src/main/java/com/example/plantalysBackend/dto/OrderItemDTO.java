package com.example.plantalysBackend.dto;

public class OrderItemDTO {
	
	private Long id;
    private String plantName;
    private String plantImage;
    private int quantity;
    private double unitPrice;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPlantName() {
		return plantName;
	}
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	public String getPlantImage() {
		return plantImage;
	}
	public void setPlantImage(String plantImage) {
		this.plantImage = plantImage;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
    
	

}
