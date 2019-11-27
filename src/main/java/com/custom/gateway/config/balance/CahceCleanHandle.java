package com.custom.gateway.config.balance;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class CahceCleanHandle {
    @Autowired
    private CustomCacheLoadBalanceHandle customerCacheLoadBalanceHandle;

    @Pointcut(value = "@annotation(com.custom.gateway.config.annotation.CacheClean)")
    public void excudeService() {
    }

    @After("excudeService()")
    public void methodAfter() {
        customerCacheLoadBalanceHandle.handle();
    }
}
