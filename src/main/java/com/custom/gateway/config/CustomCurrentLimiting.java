package com.custom.gateway.config;

import com.custom.gateway.model.core.AllowedEntity;
import com.custom.gateway.service.CurrentLimitingGlobalService;
import com.custom.gateway.service.CurrentLimitingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

@Log4j2
public class CustomCurrentLimiting {

    private RedisScript<List<Long>> script;

    private ReactiveRedisTemplate redisTemplate;

    private CurrentLimitingService service;

    private CurrentLimitingGlobalService globalService;

    public CustomCurrentLimiting(RedisScript<List<Long>> script, ReactiveRedisTemplate redisTemplate, CurrentLimitingService service
            , CurrentLimitingGlobalService globalService) {
        this.script = script;
        this.redisTemplate = redisTemplate;
        this.service = service;
        this.globalService = globalService;

    }


    public AllowedEntity isAllowed(ServerHttpRequest request) {
        if (request == null) {
            log.error("request is null!");
            return null;
        }
        return isAllowed(request.getRemoteAddress().getHostString(), request.getPath().toString());
    }

    public AllowedEntity isAllowed(String ip, String path) {

        return new AllowedEntity();
    }
}
