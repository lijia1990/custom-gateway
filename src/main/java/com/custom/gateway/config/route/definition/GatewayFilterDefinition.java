package com.custom.gateway.config.route.definition;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class GatewayFilterDefinition {
        @NotNull
        private String name;
        private Map<String, String> args = new LinkedHashMap<>();

}
