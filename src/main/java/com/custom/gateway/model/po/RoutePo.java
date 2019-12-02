package com.custom.gateway.model.po;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.jmx.snmp.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "route")
@EqualsAndHashCode(callSuper = false)
public class RoutePo {

    public RoutePo() {

    }

    public RoutePo(Long id, Boolean isDel) {
        this.id = id;
        this.isDel = isDel;
    }

    public RoutePo(RouteDefinition routeDefinition) {
        this.uri = routeDefinition.getUri().toString();
        this.routeOrder = routeDefinition.getOrder();
        if (routeDefinition.getPredicates() != null && !routeDefinition.getPredicates().isEmpty())
            this.predicates = JSONArray.toJSONString(routeDefinition.getPredicates());
        if (routeDefinition.getFilters() != null && !routeDefinition.getFilters().isEmpty())
            this.filters = JSONArray.toJSONString((routeDefinition.getFilters()));
    }

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    private Boolean isDel;

    private Date createTime;

    private Date updateTime;

    private String createId;

    private String updateId;
}