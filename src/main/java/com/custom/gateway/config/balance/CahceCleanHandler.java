package com.custom.gateway.config.balance;

import com.custom.gateway.config.annotation.CacheClean;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class CahceCleanHandler {
    @Autowired
    private CustomCacheLoadBalanceHandler customerCacheLoadBalanceHandle;

    @Pointcut(value = "@annotation(com.custom.gateway.config.annotation.CacheClean)")
    public void excudeService() {
    }

    @After("excudeService() && @annotation(cacheClean)")
    public void methodAfter(CacheClean cacheClean) {
        customerCacheLoadBalanceHandle.handle(cacheClean.value());
    }
}
