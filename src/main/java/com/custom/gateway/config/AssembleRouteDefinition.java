package com.custom.gateway.config;

import com.custom.gateway.config.route.definition.GatewayFilterDefinition;
import com.custom.gateway.config.route.definition.GatewayPredicateDefinition;
import com.custom.gateway.config.route.definition.GatewayRouteDefinition;
import com.custom.gateway.model.po.LimitingRuleGlobalPo;
import com.custom.gateway.model.po.LimitingRulePo;
import com.custom.gateway.model.po.RoutePo;
import com.custom.gateway.model.vo.LimitingRuleGlobalVo;
import com.custom.gateway.model.vo.LimitingRuleVo;
import com.custom.gateway.model.vo.RouteVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AssembleRouteDefinition {
    public static RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gwdefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gwdefinition.getId());
        definition.setOrder(gwdefinition.getOrder());
        //设置断言
        definition.setPredicates(gwdefinition.getPredicates().stream().map(pr -> {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(pr.getArgs());
            predicate.setName(pr.getName());
            return predicate;
        }).collect(Collectors.toList()));
        //设置过滤器
        definition.setFilters( gwdefinition.getFilters().stream().map(gatewayFilter->{
            FilterDefinition filter = new FilterDefinition();
            filter.setName(gatewayFilter.getName());
            filter.setArgs(gatewayFilter.getArgs());
            return filter;
        }).collect(Collectors.toList()));

        URI uri;
        if (gwdefinition.getUri().startsWith("http")) {
            uri = UriComponentsBuilder.fromHttpUrl(gwdefinition.getUri()).build().toUri();
        } else {
            // uri为 lb://consumer-service 时使用下面的方法
            uri = URI.create(gwdefinition.getUri());
        }
        definition.setUri(uri);
        return definition;
    }

    public static final Function<RoutePo, RouteVo> instanceInfoToRouteVOFunc = instance -> {
        RouteVo instanceVo = new RouteVo();
        BeanUtils.copyProperties(instance, instanceVo);
        return instanceVo;
    };
    public static final Function<LimitingRulePo, LimitingRuleVo> instanceInfoToCustomRouteVOFunc = instance -> {
        LimitingRuleVo instanceVo = new LimitingRuleVo();
        BeanUtils.copyProperties(instance, instanceVo);
        return instanceVo;
    };
    public static final Function<LimitingRuleGlobalPo, LimitingRuleGlobalVo> instanceGlobalInfoToCustomRouteVOFunc = instance -> {
        LimitingRuleGlobalVo instanceVo = new LimitingRuleGlobalVo();
        BeanUtils.copyProperties(instance, instanceVo);
        return instanceVo;
    };

    public static Boolean isAllowed(LimitingRuleGlobalPo mono) {
        if (mono != null) { //判断是否有自定义限流
            if (mono.getLimitingStartTime() == -1
                    && mono.getLimitingEndTime() == -1) //判断是否有开启关闭时间
            {
                return true;
            } else if (mono.getLimitingStartTime() <= System.currentTimeMillis()
                    && mono.getLimitingEndTime() >= System.currentTimeMillis()) //判断当前时间是否在限制时间内
            {
                return true;

            } else if (mono.getLimitingStartTime() <= System.currentTimeMillis()
                    && mono.getLimitingEndTime() == -1) //判断是否只有开启时间 没有关闭时间
            {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
