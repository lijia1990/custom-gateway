package com.custom.gateway.model.vo;

import lombok.Data;

@Data
public class RouteVo {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 断言JSON
     */
    private String predicates;
    /**
     * 拦截规则JSON
     */
    private String filters;
    /**
     * 转向URL
     */
    private String uri;
    /**
     * 排序
     */
    private Integer routeOrder;

}