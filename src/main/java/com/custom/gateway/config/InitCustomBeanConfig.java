package com.custom.gateway.config;

import com.custom.gateway.config.redis.CustomRedisScript;
import com.custom.gateway.service.CurrentLimitingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.reactive.DispatcherHandler;

import java.util.List;

@Configuration
@AutoConfigureAfter(RedisReactiveAutoConfiguration.class)
@ConditionalOnBean(ReactiveRedisTemplate.class)
@ConditionalOnClass({RedisTemplate.class, DispatcherHandler.class})
public class InitCustomBeanConfig {
    public static final String REDIS_SCRIPT_BEAN = "customRedisScript";

    @Bean(REDIS_SCRIPT_BEAN)
    @SuppressWarnings("unchecked")
    public CustomRedisScript redisRequestRateLimiterScript() {
        CustomRedisScript redisScript = new CustomRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("META-INF/scripts/request_current_limiting.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }


    @Bean
    public CustomCurrentLimiting getTool(@Qualifier(REDIS_SCRIPT_BEAN) RedisScript<List<Long>> script,
                                         ReactiveRedisTemplate<String, String> redisTemplate,
                                         CurrentLimitingService service) {
        return new CustomCurrentLimiting(script, redisTemplate, service);

    }
}
