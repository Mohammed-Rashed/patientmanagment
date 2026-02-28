package com.topbits.patientmanagment.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RedisTestController {
    private final StringRedisTemplate redis;
    public RedisTestController(StringRedisTemplate redis){
        this.redis=redis;
    }

    @GetMapping("/redis-test")
    public String test() {
        redis.opsForValue().set("test:key", "hello");
        return redis.opsForValue().get("test:key");
    }

}

