package com.example.plantalysBackend.dto;

public class UserSummaryDTO {
	private String email;

    public UserSummaryDTO() {}

    public UserSummaryDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
