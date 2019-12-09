package com.custom.gateway.config;

import com.custom.gateway.config.redis.CustomRedisGlobalScript;
import com.custom.gateway.config.redis.CustomRedisScript;
import com.custom.gateway.service.CurrentLimitingGlobalService;
import com.custom.gateway.service.CurrentLimitingService;
import com.custom.gateway.service.RouteService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
@AutoConfigureAfter(RedisReactiveAutoConfiguration.class)
public class CustomLimitingBeanConfig {
    public static final String REDIS_SCRIPT_BEAN = "customRedisScript";
    public static final String REDIS_SCRIPT_GLOBAL_BEAN = "globalCustomRedisScript";
    public static final String CURRENT_NAME = "current";
    public static final String CURRENT_SERVER_NAME = "serverLimit";

    @Bean(REDIS_SCRIPT_BEAN)
    public CustomRedisScript redisRequestRateLimiterScript() {
        CustomRedisScript redisScript = new CustomRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("script/request-current-limiting.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean(REDIS_SCRIPT_GLOBAL_BEAN)
    public CustomRedisGlobalScript redisRequestRateLimiterGlobalScript() {
        CustomRedisGlobalScript redisScript = new CustomRedisGlobalScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("script/request-current-limiting-global.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean(CURRENT_NAME)
    @ConditionalOnMissingBean
    public CustomLimiting getTool(@Qualifier(REDIS_SCRIPT_BEAN) RedisScript<Boolean> script,
                                         @Qualifier(REDIS_SCRIPT_GLOBAL_BEAN) RedisScript<Boolean> globalScript,
                                         StringRedisTemplate redisTemplate,
                                         CurrentLimitingService service, CurrentLimitingGlobalService globalService) {
        return new CustomCurrentLimiting(script, globalScript, redisTemplate, service, globalService);

    }

    @Bean(CURRENT_SERVER_NAME)
    @ConditionalOnProperty(prefix = "routeCache", name = "server", havingValue = "true")
    public CustomLimiting getServerTool(
            @Qualifier(REDIS_SCRIPT_BEAN) RedisScript<Boolean> script,
            StringRedisTemplate redisTemplate,
            RouteService routeService) {
        return new CustomServerLimiting(routeService, script, redisTemplate);

    }

}
