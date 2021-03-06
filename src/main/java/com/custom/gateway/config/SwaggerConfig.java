package com.custom.gateway.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfoBuilder().
                description("自定义网关层")
                .title("com.custom")
        .version("1.0.0").build()).select().apis(RequestHandlerSelectors.basePackage("com.xituan.portal.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
