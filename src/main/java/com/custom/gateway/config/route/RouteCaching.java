package com.custom.gateway.config.route;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteCaching {
    @Bean
    @ConditionalOnProperty(prefix = "routeCache", name = "enable", havingValue = "true")
    public RouteDefinitionRepository getRoute() {
        return new DbCachingRouteLocator();
    }





}
