package com.custom.gateway.config;

import com.custom.gateway.config.redis.CustomRedisScript;
import com.custom.gateway.service.CurrentLimitingGlobalService;
import com.custom.gateway.service.CurrentLimitingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

@Configuration
@AutoConfigureAfter(RedisReactiveAutoConfiguration.class)
public class InitCustomBeanConfig {
    public static final String REDIS_SCRIPT_BEAN = "customRedisScript";

    @Bean(REDIS_SCRIPT_BEAN)
    public CustomRedisScript redisRequestRateLimiterScript() {
        CustomRedisScript redisScript = new CustomRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("META-INF/scripts/request_current_limiting.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }


    @Bean
    @ConditionalOnMissingBean
    public CustomCurrentLimiting getTool(@Qualifier(REDIS_SCRIPT_BEAN) RedisScript<List<Long>> script,
                                         ReactiveRedisTemplate<String, String> redisTemplate,
                                         CurrentLimitingService service,CurrentLimitingGlobalService globalService) {
        return new CustomCurrentLimiting(script, redisTemplate, service,globalService);

    }
}
