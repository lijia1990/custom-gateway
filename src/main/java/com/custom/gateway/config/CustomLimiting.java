package com.custom.gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface CustomLimiting {
    Mono<Boolean> isAllowed(ServerHttpRequest request);
}
