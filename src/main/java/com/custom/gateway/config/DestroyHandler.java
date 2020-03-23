package com.custom.gateway.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Log4j2
public class DestroyHandler {

    @PreDestroy
    public void destroy() {
        log.info("------Service closing------");
    }
}
