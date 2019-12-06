package com.custom.gateway.controller;

import com.custom.gateway.config.AssembleRouteDefinition;
import com.custom.gateway.config.annotation.CacheClean;
import com.custom.gateway.config.annotation.CustomRestController;
import com.custom.gateway.config.route.definition.GatewayRouteDefinition;
import com.custom.gateway.model.core.BaseAdvice;
import com.custom.gateway.model.core.PageBean;
import com.custom.gateway.model.core.ResponseBodyEntity;
import com.custom.gateway.model.form.RouteQueryForm;
import com.custom.gateway.model.vo.RouteVo;
import com.custom.gateway.service.DynamicRouteService;
import com.custom.gateway.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import static com.custom.gateway.config.AssembleRouteDefinition.assembleRouteDefinition;

@CustomRestController("/gateway/route")
@Import(AssembleRouteDefinition.class)
public class RouteController extends BaseAdvice {

    @Autowired
    private DynamicRouteService dynamicRouteService;

    @Autowired
    private RouteService service;

    @PostMapping("/add")
    public Mono<ResponseBodyEntity<String>> add(@RequestBody GatewayRouteDefinition gwdefinition) {
        dynamicRouteService.add(assembleRouteDefinition(gwdefinition));
        return Mono.just(ResponseBodyEntity.success());
    }

    @GetMapping("/delete/{id}")
    public Mono<ResponseBodyEntity<String>> delete(@PathVariable String id) {
        this.dynamicRouteService.delete(id);
        return Mono.just(ResponseBodyEntity.success());
    }

    @PostMapping("/update")
    public Mono<ResponseBodyEntity<String>> update(@RequestBody GatewayRouteDefinition gwdefinition) {
        this.dynamicRouteService.update(assembleRouteDefinition(gwdefinition));
        return Mono.just(ResponseBodyEntity.success());
    }

    @PostMapping("queryForList")
    public Mono<ResponseBodyEntity<PageBean<RouteVo>>> queryForList(@RequestBody RouteQueryForm form) {
        return service.queryForList(form).map(ResponseBodyEntity::success);
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseBodyEntity<PageBean<RouteVo>>> find(@PathVariable Long id) {
        return service.findById(id).map(ResponseBodyEntity::success);
    }

    @GetMapping("clean")
    public Mono<ResponseBodyEntity<String>> clean(String value) {
        service.clean(value);
        dynamicRouteService.refreshRoute();
        return Mono.just(ResponseBodyEntity.success());
    }

    @GetMapping("/test")
    @CacheClean
    public Mono<Integer> test() {
        return Mono.just(200);
    }
}
