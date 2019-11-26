package com.custom.gateway.config.route.definition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GatewayRouteDefinition {
    private String id;
    private List<GatewayPredicateDefinition> predicates = new ArrayList<>();
    private List<GatewayFilterDefinition> filters = new ArrayList<>();
    private String uri;
    private int order = 0;
}
