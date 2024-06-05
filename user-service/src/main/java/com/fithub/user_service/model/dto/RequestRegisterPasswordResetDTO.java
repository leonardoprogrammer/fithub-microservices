package com.fithub.user_service.model.dto;

public class RequestRegisterPasswordResetDTO {

    private String email;

    public RequestRegisterPasswordResetDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
