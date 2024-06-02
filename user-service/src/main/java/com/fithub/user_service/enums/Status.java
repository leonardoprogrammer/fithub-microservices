package com.fithub.user_service.enums;

public enum Status {

    ACTIVE("A"),
    DEACTIVATED("D");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
