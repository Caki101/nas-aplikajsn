package com.backend.Backend.services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ConfirmationService {
    private final StringRedisTemplate stringRedisTemplate;
    private final String REDIS_PREFIX = "confirm:";

    public ConfirmationService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String generateToken() {
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(REDIS_PREFIX+token, "pending", 60, TimeUnit.SECONDS);
        return token;
    }

    public boolean confirmToken(String token) {
        String key = REDIS_PREFIX + token;
        if (stringRedisTemplate.hasKey(key)) {
            stringRedisTemplate.convertAndSend("confirmation-channel", token);
            stringRedisTemplate.delete(key);
            return true;
        }
        return false;
    }
}
