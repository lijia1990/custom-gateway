package com.custom.gateway.config.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface CustomRestController {
    @AliasFor("path")
    String[] value() default {};
    @AliasFor("value")
    String[] path() default {};

}
