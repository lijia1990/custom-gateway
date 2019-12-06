package com.custom.gateway.config.balance;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public class ReactiveRequestContextHolder {
    static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;
    private static final ThreadLocal<String> ip = new ThreadLocal<>();

    ReactiveRequestContextHolder() {

    }

    public static void setIp(String id) {
        ip.set(id);
    }

    public static String getIp() {
        return ip.get();
    }

    public static void clean() {
        ip.remove();
    }

    public static Mono<String> getRequest() {
        return Mono.subscriberContext()
                .map(ctx -> ctx.get(CONTEXT_KEY).getRemoteAddress().getHostString());
    }

    public static Mono<ServerHttpRequest> getContextRequest() {
        return Mono.subscriberContext()
                .map(ctx -> ctx.get(CONTEXT_KEY));
    }

}