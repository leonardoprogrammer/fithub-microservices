package com.fithub.user_service.model.dto;

public class RequestUpdatePasswordDTO {

    private String requiredPassword;
    private String newPassword;

    public RequestUpdatePasswordDTO() {
    }

    public String getRequiredPassword() {
        return requiredPassword;
    }

    public void setRequiredPassword(String requiredPassword) {
        this.requiredPassword = requiredPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
