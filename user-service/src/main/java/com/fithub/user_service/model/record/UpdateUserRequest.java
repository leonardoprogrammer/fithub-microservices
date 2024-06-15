package com.fithub.user_service.model.record;

import java.sql.Date;

public record UpdateUserRequest(String name, Date dateBirth, String gender, String email,
                                String requiredPassword) {
}
