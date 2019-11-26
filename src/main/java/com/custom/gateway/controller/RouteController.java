package com.custom.gateway.controller;

import com.custom.gateway.config.AssembleRouteDefinition;
import com.custom.gateway.config.route.definition.GatewayRouteDefinition;
import com.custom.gateway.model.core.BaseAdvice;
import com.custom.gateway.model.core.ResponseBodyEntity;
import com.custom.gateway.model.form.XtRouteQueryForm;
import com.custom.gateway.model.vo.RouteVo;
import com.custom.gateway.service.DynamicRouteService;
import com.custom.gateway.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.custom.gateway.config.AssembleRouteDefinition.assembleRouteDefinition;

@RestController
@RequestMapping("/gateway/route")
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

    @GetMapping("/routes/{id}")
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
    public Mono<ResponseBodyEntity<RouteVo>> find(@RequestBody XtRouteQueryForm form) {
        return  service.queryForList(form).map(ResponseBodyEntity::success);
    }

    @GetMapping("clean")
    public Mono<ResponseBodyEntity<String>> clean() {
        service.clean();
        dynamicRouteService.refreshRoute();
        return Mono.just(ResponseBodyEntity.success());
    }
}
