package com.custom.gateway.service;

import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Mono;

public interface DynamicRouteService {

    Mono<Void> add(RouteDefinition definition);

    Mono<Void> update(RouteDefinition definition);

    Mono<Void> delete(String id);

    void refreshRoute();
}
