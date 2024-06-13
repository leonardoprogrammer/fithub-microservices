package com.fithub.auth_service.enums;

public enum Roles {

    BASIC("BASIC");

    private String value;

    Roles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
