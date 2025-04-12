package com.backend.Backend.services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ConfirmationService {
    private final StringRedisTemplate stringRedisTemplate;
    private final String CONFIRMATION_PREFIX = "confirm:";
    private final String USER_PREFIX = "user:";

    public ConfirmationService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String generateToken(String email) {
        String existing_token = stringRedisTemplate.opsForValue().get(USER_PREFIX + email);
        if (existing_token != null) {
            stringRedisTemplate.delete(CONFIRMATION_PREFIX + existing_token);
        }

        // need more secure token
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(CONFIRMATION_PREFIX+token, "pending", 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(USER_PREFIX + email, token);

        return token;
    }

    public boolean confirmToken(String token) {
        String key = CONFIRMATION_PREFIX + token;
        if (stringRedisTemplate.hasKey(key)) {
            stringRedisTemplate.convertAndSend("confirmation-channel", token);
            stringRedisTemplate.delete(key);
            return true;
        }
        return false;
    }
}
