package com.custom.gateway.service;

import com.custom.gateway.model.core.PageBean;
import com.custom.gateway.model.form.XtRouteQueryForm;
import com.custom.gateway.model.po.RoutePo;
import com.custom.gateway.model.vo.RouteVo;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RouteService {

    void save(RoutePo po);

    void delete(Long id);

    void update(RoutePo po);

    RoutePo findById(Long id);

    List<RouteVo> queryList();

    Mono<PageBean<RouteVo>> queryForList(XtRouteQueryForm form);

    void clean();
}
