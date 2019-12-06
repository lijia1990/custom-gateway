package com.custom.gateway.config;

import com.custom.gateway.config.redis.CustomRedisGlobalScript;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
@AutoConfigureAfter(RedisReactiveAutoConfiguration.class)
public class InitCustomBeanConfig {
    public static final String REDIS_SCRIPT_BEAN = "customRedisScript";
    public static final String REDIS_SCRIPT_GLOBAL_BEAN = "globalCustomRedisScript";

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

    @Bean
    @ConditionalOnMissingBean
    public CustomCurrentLimiting getTool(@Qualifier(REDIS_SCRIPT_BEAN) RedisScript<Boolean> script,
                                         @Qualifier(REDIS_SCRIPT_GLOBAL_BEAN) RedisScript<Boolean> globalScript,
                                         StringRedisTemplate redisTemplate,
                                         CurrentLimitingService service, CurrentLimitingGlobalService globalService) {
        return new CustomCurrentLimiting(script, globalScript, redisTemplate, service, globalService);

    }
}
