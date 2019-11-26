package com.custom.gateway.config.balance;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class CacheAspectHandle {
    private static final String DATA_PONIT = "execution(* com.xituan.portal.service.impl.*.*(..))";
    private static final List METHOD_LIST= Arrays.asList("save,update,delete");
    @Autowired
    private CustomCacheLoadBalanceHandle customerCacheLoadBalanceHandle;
    @Pointcut(DATA_PONIT)
    public void excute() {
    }

    @AfterReturning(value = DATA_PONIT)
    public void methodAfter(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (METHOD_LIST.contains(method.getName())){
            customerCacheLoadBalanceHandle.handle();
        }
    }

    @Before("excute()")
    public void methodBefore(JoinPoint joinPoint) {

    }

}