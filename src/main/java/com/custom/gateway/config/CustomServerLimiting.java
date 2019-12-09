package com.custom.gateway.config;

import com.custom.gateway.service.RouteService;
import com.netflix.discovery.EurekaClient;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class CustomServerLimiting implements CustomLimiting {

    private PathPatternParser pathPatternParser;

    private RedisScript<Boolean> script;

    private EurekaClient eurekaClient;

    private RouteService routeService;

    private StringRedisTemplate redisTemplate;

    public CustomServerLimiting() {

    }

    public CustomServerLimiting(RouteService service, RedisScript<Boolean> script, StringRedisTemplate redisTemplate) {
        this.routeService = service;
        this.script = script;
        this.redisTemplate = redisTemplate;
        this.pathPatternParser = new PathPatternParser();
    }

    public Mono<Boolean> apply(Config config) {
        final ArrayList<PathPattern> pathPatterns = new ArrayList<>();
        synchronized (this.pathPatternParser) {
            pathPatternParser.setMatchOptionalTrailingSeparator(
                    config.isMatchOptionalTrailingSeparator());
            config.getPatterns().forEach(pattern -> {
                PathPattern pathPattern = this.pathPatternParser.parse(pattern);
                pathPatterns.add(pathPattern);
            });
        }
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> isAllowed(ServerHttpRequest request) {
        return null;
    }


    @Validated
    public static class Config {

        private List<String> patterns = new ArrayList<>();

        private boolean matchOptionalTrailingSeparator = true;

        @Deprecated
        public String getPattern() {
            if (!CollectionUtils.isEmpty(this.patterns)) {
                return patterns.get(0);
            }
            return null;
        }

        @Deprecated
        public CustomServerLimiting.Config setPattern(String pattern) {
            this.patterns = new ArrayList<>();
            this.patterns.add(pattern);
            return this;
        }

        public List<String> getPatterns() {
            return patterns;
        }

        public CustomServerLimiting.Config setPatterns(List<String> patterns) {
            this.patterns = patterns;
            return this;
        }

        public boolean isMatchOptionalTrailingSeparator() {
            return matchOptionalTrailingSeparator;
        }

        public CustomServerLimiting.Config setMatchOptionalTrailingSeparator(
                boolean matchOptionalTrailingSeparator) {
            this.matchOptionalTrailingSeparator = matchOptionalTrailingSeparator;
            return this;
        }

        @Override
        public String toString() {
            return new ToStringCreator(this).append("patterns", patterns)
                    .append("matchOptionalTrailingSeparator",
                            matchOptionalTrailingSeparator)
                    .toString();
        }

    }
}
