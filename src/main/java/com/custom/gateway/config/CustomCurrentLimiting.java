package com.custom.gateway.config;

import com.custom.gateway.model.core.LimitingVo;
import com.custom.gateway.model.po.LimitingRuleGlobalPo;
import com.custom.gateway.model.vo.LimitingRuleGlobalVo;
import com.custom.gateway.model.vo.LimitingRuleVo;
import com.custom.gateway.service.CurrentLimitingGlobalService;
import com.custom.gateway.service.CurrentLimitingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j2
public class CustomCurrentLimiting {

    private RedisScript<Boolean> script;

    private RedisScript<Boolean> globalScript;

    private CurrentLimitingService service;

    private CurrentLimitingGlobalService globalService;

    private StringRedisTemplate stringRedisTemplate;

    public CustomCurrentLimiting(RedisScript<Boolean> script, RedisScript<Boolean> globalScript, StringRedisTemplate redisTemplate, CurrentLimitingService service
            , CurrentLimitingGlobalService globalService) {
        this.script = script;
        this.globalScript = globalScript;
        this.stringRedisTemplate = redisTemplate;
        this.service = service;
        this.globalService = globalService;

    }

    public Mono<Boolean> isAllowed(ServerHttpRequest request) {
        if (request == null) {
            log.error("request is null!");
            return null;
        }
        return isAllowed(request.getRemoteAddress().getHostString(), request.getPath().toString());
    }

    public Mono<Boolean> isAllowed(String ip, String path) {
        Mono<LimitingRuleVo> voMonoIp;
        Mono<LimitingRuleVo> voMonoPath;
        voMonoIp = service.queryForVal(ip);
        voMonoPath = service.queryForVal(path);
        if (isAllowed(voMonoIp) || isAllowed(voMonoPath)) { //判断是否有自定义限流
            return isAllowed(voMonoIp.block(), voMonoPath.block());
        } else {  //判断是否有全局限流
            Mono<Map> globalRule = globalService.queryForGlobal();
            return isAllowed(globalRule.block(), ip, path);
        }
    }

    public Mono<Boolean> isAllowed(Map ruleGlobal, String ip, String path) {
        log.info("All-restricted flow parameter splicing-------");
        if (ruleGlobal != null && ruleGlobal.size() > 0) {

            return Mono.just(false);
        } else {
            return Mono.just(true);
        }
    }

    public Mono<Boolean> isAllowed(LimitingRuleVo ipRule, LimitingRuleVo pathRule) {
        log.info("Custom current limiting parameter splicing------");
        if (ipRule != null) {
            List<String> keys = Arrays.asList(ipRule.getRuleVal(), null);
            return isAllowed(keys, ipRule.getQpsCount().toString(), null, ipRule.getLimitingHz());
        } else {
            List<String> keys = Arrays.asList(null, pathRule.getRuleVal());
            return isAllowed(keys, null, pathRule.getQpsCount().toString(), pathRule.getLimitingHz());
        }
    }

    private Boolean isAllowed(Mono<? extends LimitingVo> monoVo) {
        if (monoVo.block() != null) { //判断是否有自定义限流
            if (monoVo.block().getLimitingStartTime() == -1
                    && monoVo.block().getLimitingEndTime() == -1) //判断是否有开启关闭时间
            {
                return true;
            } else if (monoVo.block().getLimitingStartTime() <= System.currentTimeMillis()
                    && monoVo.block().getLimitingEndTime() >= System.currentTimeMillis()) //判断当前时间是否在限制时间内
            {
                return true;

            } else if (monoVo.block().getLimitingStartTime() <= System.currentTimeMillis()
                    && monoVo.block().getLimitingEndTime() == -1) //判断是否只有开启时间 没有关闭时间
            {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private Mono<Boolean> isAllowed(List<String> keys, String... vals) {
        log.info("Enter custom current limiting LUA check-----------------");
        return Mono.just(this.stringRedisTemplate.execute(this.script, keys,
                vals));
    }

}