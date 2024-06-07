package com.fithub.gateway_service.enums;

public enum ServicesEnum {

    AUTH(1, "auth-service"),
    USER(2, "user-serivce"),
    WORKOUT(3, "workout-service"),
    HISTORY(4, "history-service"),
    SHARING(5, "sharing-service"),
    DASHBOARD(6, "dashboard-service"),
    NOTIFICATION(7, "notification-service"),
    GATEWAY(8, "gateway-service");

    private Integer module;
    private String name;

    ServicesEnum(Integer module, String name) {
        this.module = module;
        this.name = name;
    }

    public Integer getModule() {
        return module;
    }

    public String getName() {
        return name;
    }
}
