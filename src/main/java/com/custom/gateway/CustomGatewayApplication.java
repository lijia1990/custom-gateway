package com.custom.gateway;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableRetry
@EnableCaching
@EnableAsync
@MapperScan("com.custom.gateway.dao")
@SpringBootApplication(scanBasePackages = "com.custom.gateway.*")
@NacosPropertySource(dataId = "customer-gateway", autoRefreshed = true)
public class CustomGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomGatewayApplication.class, args);
    }

}
