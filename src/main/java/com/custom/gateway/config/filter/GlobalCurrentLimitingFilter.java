package com.custom.gateway.config.filter;

import com.custom.gateway.config.CustomLimiting;
import com.custom.gateway.config.CustomLimitingBeanConfig;
import com.custom.gateway.config.exception.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import static com.custom.gateway.config.CustomLimitingBeanConfig.CURRENT_NAME;


@Configuration
@AutoConfigureAfter(CustomLimitingBeanConfig.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(prefix = "limiting", name = "enable", havingValue = "true")
public class GlobalCurrentLimitingFilter implements WebFilter, Ordered {

    @Autowired
    @Qualifier(CURRENT_NAME)
    private CustomLimiting customCurrentLimiting;

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
            return Mono.error(new TooManyRequestsException(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase()));
        }
        return chain.filter(exchange);
    }

}
