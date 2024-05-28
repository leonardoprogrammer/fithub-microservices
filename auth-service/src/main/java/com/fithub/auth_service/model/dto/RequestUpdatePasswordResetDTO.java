package com.fithub.auth_service.model.dto;

import java.util.UUID;

public class RequestUpdatePasswordResetDTO {

    private UUID id;
    private String password;

    public RequestUpdatePasswordResetDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
