package com.fithub.user_service.model.record;

public record UpdatePasswordRequest(String requiredPassword, String newPassword) {
}
