package com.fithub.user_service.model.dto;

public class RequestRequiredPasswordDTO {

    private String requiredPassword;

    public RequestRequiredPasswordDTO() {
    }

    public String getRequiredPassword() {
        return requiredPassword;
    }

    public void setRequiredPassword(String requiredPassword) {
        this.requiredPassword = requiredPassword;
    }
}
