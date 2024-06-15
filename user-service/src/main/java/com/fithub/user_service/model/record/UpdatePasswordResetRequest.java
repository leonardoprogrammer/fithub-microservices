package com.fithub.user_service.model.record;

import java.util.UUID;

public record UpdatePasswordResetRequest(UUID id, String password) {
}
