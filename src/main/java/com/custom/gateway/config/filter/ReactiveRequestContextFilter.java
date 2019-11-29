package com.custom.gateway.config.filter;

import com.custom.gateway.config.balance.CustomLoadBalance;
import com.custom.gateway.config.balance.ReactiveRequestContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.custom.gateway.config.balance.ReactiveRequestContextHolder.setIp;


@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Import(ReactiveRequestContextHolder.class)
@ConditionalOnBean(CustomLoadBalance.class)
public class ReactiveRequestContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        setIp(exchange.getRequest().getRemoteAddress().getHostString());
        return chain.filter(exchange);
    }
}