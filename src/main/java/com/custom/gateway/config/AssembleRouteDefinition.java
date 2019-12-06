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

public class AssembleRouteDefinition {
    public static RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gwdefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gwdefinition.getId());
        definition.setOrder(gwdefinition.getOrder());

        //设置断言
        List<PredicateDefinition> pdList = new ArrayList<>();
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList = gwdefinition.getPredicates();
        for (GatewayPredicateDefinition gpDefinition : gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }
        definition.setPredicates(pdList);

        //设置过滤器
        List<FilterDefinition> filters = new ArrayList();
        List<GatewayFilterDefinition> gatewayFilters = gwdefinition.getFilters();
        for (GatewayFilterDefinition filterDefinition : gatewayFilters) {
            FilterDefinition filter = new FilterDefinition();
            filter.setName(filterDefinition.getName());
            filter.setArgs(filterDefinition.getArgs());
            filters.add(filter);
        }
        definition.setFilters(filters);

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

    public static Function<RoutePo, RouteVo> instanceInfoToRouteVOFunc = instance -> {
        RouteVo instanceVo = new RouteVo();
        BeanUtils.copyProperties(instance, instanceVo);
        return instanceVo;
    };
    public static Function<LimitingRulePo, LimitingRuleVo> instanceInfoToCustomRouteVOFunc = instance -> {
        LimitingRuleVo instanceVo = new LimitingRuleVo();
        BeanUtils.copyProperties(instance, instanceVo);
        return instanceVo;
    };
    public static Function<LimitingRuleGlobalPo, LimitingRuleGlobalVo> instanceGlobalInfoToCustomRouteVOFunc = instance -> {
        LimitingRuleGlobalVo instanceVo = new LimitingRuleGlobalVo();
        BeanUtils.copyProperties(instance, instanceVo);
        return instanceVo;
    };

}
