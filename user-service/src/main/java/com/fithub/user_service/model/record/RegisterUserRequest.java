package com.fithub.user_service.model.record;

import java.sql.Date;

public record RegisterUserRequest(String name, Date dateBirth, String gender,
                                  RegisterLoginRequest login) {
}
