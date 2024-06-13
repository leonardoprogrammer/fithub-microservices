package com.fithub.auth_service.model.dto;

import java.util.Date;

public class LoginResponseDTO {

    private String accessToken;
    private Date expiryDate;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String accessToken, Date expiryDate) {
        this.accessToken = accessToken;
        this.expiryDate = expiryDate;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "ResponseLoginDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
