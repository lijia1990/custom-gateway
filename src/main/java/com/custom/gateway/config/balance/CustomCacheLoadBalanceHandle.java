package com.custom.gateway.config.balance;

import com.alibaba.fastjson.JSONArray;
import com.custom.gateway.config.CustomACAware;
import com.custom.gateway.model.core.ResponseBodyEntity;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static com.custom.gateway.config.CustomACAware.getBean;


@Log4j2
@Import(CustomACAware.class)
@Component
public class CustomCacheLoadBalanceHandle {
    @Value("${spring.application.name}")
    private String SERVER_NAME;

    public Boolean handle() {
        return handle(getBean(SpringClientFactory.class));
    }

    public Boolean handle(SpringClientFactory factory) {
        ILoadBalancer loadBalancer = factory.getLoadBalancer(SERVER_NAME);
        return handle((DynamicServerListLoadBalancer) loadBalancer);
    }

    public Boolean handle(DynamicServerListLoadBalancer<DiscoveryEnabledServer> balancer) {
        DomainExtractingServerList domainExtractingServerList = (DomainExtractingServerList) balancer.getServerListImpl();
        try {
            Field field = domainExtractingServerList.getClass().getDeclaredField("list");
            field.setAccessible(true);
            ServerList<DiscoveryEnabledServer> list = (ServerList<DiscoveryEnabledServer>) field.get(domainExtractingServerList);
            return handle(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public Boolean handle(ServerList<DiscoveryEnabledServer> list) {
        Boolean resultFlag = true;
        for (DiscoveryEnabledServer server : list.getUpdatedListOfServers()) {
            resultFlag = handle(server);
            if (!resultFlag) {
                return resultFlag;
            }
        }
        return resultFlag;
    }

    @Async
    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(delay = 1000 * 2, multiplier = 1.5))
    public Boolean handle(DiscoveryEnabledServer server) {
        WebClient client = WebClient.create(String.format("http://%s/", server.getHostPort()));
        Mono<ResponseBodyEntity> result = client.get()
                .uri("gateway/route/clean")
                .accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(ResponseBodyEntity.class);
        if (!result.block().getCode().equals(200)) {
            log.error("bad remote request server:{} error:{}", server.getHostPort(), JSONArray.toJSONString(result));
            return false;
        }
        return true;
    }
}
