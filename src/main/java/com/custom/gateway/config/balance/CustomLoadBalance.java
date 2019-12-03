package com.custom.gateway.config.balance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Configuration
@ConditionalOnProperty(prefix = "routeCache", name = "balance", havingValue = "true")
public class CustomLoadBalance extends AbstractLoadBalancerRule {


    @Override
    public Server choose(Object key) {
        List<Server> servers = this.getLoadBalancer().getReachableServers();
        if (servers.isEmpty()) {
            log.error("The url is empty ! request ip is : {}",ReactiveRequestContextHolder.getIp());
            return null;
        }
        Server server = servers.get(0);
        String ip = ReactiveRequestContextHolder.getIp();
        if (StringUtils.isNotBlank(ip)) {
            List<Server> serverList = servers.stream().filter(ser -> ser.getHost().equals(ip)).collect(Collectors.toList());
            if (!serverList.isEmpty()){
                server=serverList.get(0);
            }
        }
        ReactiveRequestContextHolder.clean();
        return server;
    }


    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

}