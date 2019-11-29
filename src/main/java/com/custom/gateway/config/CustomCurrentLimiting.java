package com.custom.gateway.config;

import com.custom.gateway.service.CurrentLimitingService;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

public class CustomCurrentLimiting {

    private RedisScript<List<Long>> script;

    private ReactiveRedisTemplate redisTemplate;

    private CurrentLimitingService service;

    public CustomCurrentLimiting(RedisScript<List<Long>> script, ReactiveRedisTemplate redisTemplate,CurrentLimitingService service) {
        this.script = script;
        this.redisTemplate = redisTemplate;
        this.service=service;
    }


    public Boolean isAllowed() {
        return null;
    }
}
