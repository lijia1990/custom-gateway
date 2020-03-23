package com.custom.gateway.config.balance;

import com.alibaba.fastjson.JSONArray;
import com.custom.gateway.config.CustomACAware;
import com.custom.gateway.config.exception.BadRequestException;
import com.google.common.collect.Maps;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.custom.gateway.config.CustomACAware.getBean;


@Log4j2
@Import(CustomACAware.class)
@Component
public class CustomCacheLoadBalanceHandler {
    @Value("${spring.application.name}")
    private String serverName;

    public Boolean handle(String key) {
        return handle(getBean(SpringClientFactory.class), key);
    }

    public Boolean handle(SpringClientFactory factory, String key) {
        ILoadBalancer loadBalancer = factory.getLoadBalancer(serverName);
        return handle((DynamicServerListLoadBalancer) loadBalancer, key);
    }

    public Boolean handle(DynamicServerListLoadBalancer<DiscoveryEnabledServer> balancer, String key) {
        DomainExtractingServerList domainExtractingServerList = (DomainExtractingServerList) balancer.getServerListImpl();
        try {
            Field field = domainExtractingServerList.getClass().getDeclaredField("list");
            field.setAccessible(true);
            ServerList<DiscoveryEnabledServer> list = (ServerList<DiscoveryEnabledServer>) field.get(domainExtractingServerList);
            return handle(list, key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public Boolean handle(ServerList<DiscoveryEnabledServer> list, String key) {
        Boolean resultFlag = true;
        HashMap value = Maps.newHashMap();
        value.put("value", key);
        for (DiscoveryEnabledServer server : list.getUpdatedListOfServers()) {
            try {
                resultFlag = handle(server, value);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
            if (!resultFlag) {
                return resultFlag;
            }
        }
        return resultFlag;
    }

    @Async
    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(delay = 1000 * 2, multiplier = 1.5))
    public Boolean handle(DiscoveryEnabledServer server, Map value) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> resultMap = restTemplate.getForObject(String.format("http://%s/gateway/route/clean", server.getHostPort()), Map.class, value);
        if (!(Boolean) resultMap.get("success")) {
            throw new BadRequestException(String.format("bad request:%s", JSONArray.toJSONString(resultMap)));
        }
        return true;
    }
}
