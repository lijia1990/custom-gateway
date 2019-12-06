package com.custom.gateway.config.filter;

import com.custom.gateway.config.CustomCurrentLimiting;
import com.custom.gateway.config.InitCustomBeanConfig;
import com.custom.gateway.model.core.AllowedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Configuration
@AutoConfigureAfter(InitCustomBeanConfig.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(prefix = "limiting", name = "enable", havingValue = "true")
public class GlobalCurrentLimitingFilter implements WebFilter, Ordered {
    @Autowired
    private CustomCurrentLimiting customCurrentLimiting;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return customCurrentLimiting.isAllowed(exchange.getRequest()).flatMap(data -> filter(exchange, chain, data)
        );
    }

    public static Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain, Boolean data) {
        if (data != null && !data) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

}
