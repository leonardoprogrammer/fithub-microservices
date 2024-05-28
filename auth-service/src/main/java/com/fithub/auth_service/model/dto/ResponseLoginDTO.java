package com.fithub.auth_service.model.dto;

public class ResponseLoginDTO {

    private String token;

    public ResponseLoginDTO() {
    }

    public ResponseLoginDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ResponseLoginDTO{" +
                "token='" + token + '\'' +
                '}';
    }
}
