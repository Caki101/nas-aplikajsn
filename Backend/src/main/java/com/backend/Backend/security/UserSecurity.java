package com.backend.Backend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserSecurity {
    private final static BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();

    public static String encryptPassword(String password) {
        return encoder.encode(password);
    }

    public static Boolean verifyPassword(String password, String hashed_password) {
        return encoder.matches(password, hashed_password);
    }
}