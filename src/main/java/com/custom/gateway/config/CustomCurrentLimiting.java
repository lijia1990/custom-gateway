package com.custom.gateway.config;

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

@Log4j2
public class CustomCurrentLimiting {

    private RedisScript<Boolean> script;

    private CurrentLimitingService service;

    private CurrentLimitingGlobalService globalService;

    private StringRedisTemplate stringRedisTemplate;

    public CustomCurrentLimiting(RedisScript<Boolean> script, StringRedisTemplate redisTemplate, CurrentLimitingService service
            , CurrentLimitingGlobalService globalService) {
        this.script = script;
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
            Mono<LimitingRuleGlobalVo> globalRule = globalService.queryForGlobal();
        }
        return Mono.empty();
    }

    public Mono<Boolean> isAllowed(LimitingRuleVo ipRule, LimitingRuleVo pathRule) {
        log.info("自定义限流参数拼接-------");
        if (ipRule != null) {
            List<String> keys = Arrays.asList(ipRule.getRuleVal(), null);
            return isAllowed(keys, ipRule.getQpsCount().toString(), null, ipRule.getLimitingHz());
        } else {
            List<String> keys = Arrays.asList(null, pathRule.getRuleVal());
            return isAllowed(keys, null, pathRule.getQpsCount().toString(), pathRule.getLimitingHz());
        }
    }

    private Boolean isAllowed(Mono<LimitingRuleVo> monoVo) {
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
        log.info("进入自定义限流 LUA校验-----------------");
        return Mono.just(this.stringRedisTemplate.execute(this.script, keys,
                vals));
    }

}