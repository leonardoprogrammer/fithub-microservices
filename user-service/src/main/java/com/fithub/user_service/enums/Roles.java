package com.fithub.user_service.enums;

public enum Roles {

    BASIC(1L);

    Long roleId;

    Roles(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }
}
