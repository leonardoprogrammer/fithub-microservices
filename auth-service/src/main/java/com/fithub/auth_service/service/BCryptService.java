package com.fithub.auth_service.service;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptService extends BCrypt {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
