package com.custom.gateway.config.route;

import com.alibaba.fastjson.JSONArray;
import com.custom.gateway.config.CustomACAware;
import com.custom.gateway.model.po.RoutePo;
import com.custom.gateway.model.vo.RouteVo;
import com.custom.gateway.service.RouteService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class DbCachingRouteLocator implements RouteDefinitionRepository {

    private RouteService service;
    public RouteService getDao() {
        service = CustomACAware.getBean(RouteService.class);
        return service;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        getDao();
        List<RouteVo> list = service.queryList();
        return Flux.fromIterable(list.stream().map(param -> {
            RouteDefinition rd = new RouteDefinition();
            rd.setId(param.getId().toString());
            rd.setOrder(param.getRouteOrder());
            try {
                rd.setUri(new URI(param.getUri()));
            } catch (URISyntaxException e) {
                log.error(e.getMessage(), e);
            }
            if (StringUtils.isNotBlank(param.getFilters())) {
                rd.setFilters(JSONArray.parseArray(param.getFilters(), FilterDefinition.class));
            }
            if (StringUtils.isNotBlank(param.getPredicates())) {
                rd.setPredicates(JSONArray.parseArray(param.getPredicates(), PredicateDefinition.class));
            }
            return rd;
        }).collect(Collectors.toList()));
    }
    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition -> {
            service.save(new RoutePo(routeDefinition));
            return Mono.empty();
        });
    }
    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(rId -> {
                    if (Strings.isNotBlank(rId)) {
                        service.delete(Long.valueOf(rId));
                        return Mono.empty();
                    }
                    return Mono.defer(() -> Mono.error(
                            new NotFoundException("RouteDefinition not found: " + routeId)));
                }
        );
    }
}
