package com.backend.Backend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {
    private final BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();

    public String encryptPassword(String password) {
        return encoder.encode(password);
    }

    public Boolean verifyPassword(String password, String hashed_password) {
        return encoder.matches(password, hashed_password);
    }
}