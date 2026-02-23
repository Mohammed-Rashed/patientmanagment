package com.topbits.patientmanagment.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final StringRedisTemplate redis;
    private final Duration ttl = Duration.ofDays(14);
    public RefreshTokenService(StringRedisTemplate redis){
        this.redis=redis;
    }

    public String issue(String email) {
        String token = UUID.randomUUID().toString() + "." + UUID.randomUUID();
        redis.opsForValue().set(key(token), email, ttl);
        return token;
    }

    public String getEmailOrThrow(String token) {
        String email = redis.opsForValue().get(key(token));
        if (email == null) {
            throw new RuntimeException("INVALID_REFRESH_TOKEN");
        }
        return email;
    }

    public void revoke(String token) {
        redis.delete(key(token));
    }
    private String key(String token) {
        return "rt:" + token;
    }

}
