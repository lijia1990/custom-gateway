package com.custom.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.custom.gateway.config.AssembleRouteDefinition;
import com.custom.gateway.config.annotation.CacheClean;
import com.custom.gateway.dao.RouteMapper;
import com.custom.gateway.model.core.PageBean;
import com.custom.gateway.model.form.XtRouteQueryForm;
import com.custom.gateway.model.po.RoutePo;
import com.custom.gateway.model.vo.RouteVo;
import com.custom.gateway.service.RouteService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.custom.gateway.config.AssembleRouteDefinition.instanceInfoToXtRouteVOFunc;

@Service
@CacheConfig(cacheNames = "xtRoute")
@Transactional(rollbackFor = RuntimeException.class)
@Import(AssembleRouteDefinition.class)
public class RouteServiceImpl implements RouteService {


    @Autowired
    private RouteMapper dao;


    @Override
    @CacheClean
    @CachePut(value = "xtRoute", key = "#po.id")
    public void save(RoutePo po) {
        dao.insert(po);
    }

    @Override
    @CacheClean
    @CacheEvict(value = "xtRoute", key = "#id")
    public void delete(Long id) {
        RoutePo po = new RoutePo(id, true);
        dao.updateById(po);
    }

    @Override
    @CacheClean
    @CacheEvict(value = "xtRoute", key = "#po.id")
    public void update(RoutePo po) {
        dao.updateById(po);
    }

    @Override
    @Cacheable(key = "#id")
    public RoutePo findById(Long id) {
        return null;
    }

    @Override
    @Cacheable(value = "RouteList")
    public List<RouteVo> queryList() {
        return  dao.selectList(null).stream().map(instanceInfoToXtRouteVOFunc).collect(Collectors.toList());
    }

    @Override
    public Mono<PageBean<RouteVo>> queryForList(XtRouteQueryForm form) {
        PageHelper.startPage(form.getPageNo(), form.getPageSize());
        List<RoutePo> list = dao.selectList(new QueryWrapper<RoutePo>()
                .eq("gender", 0));
        return Mono.just(new PageBean(list.stream().map(instanceInfoToXtRouteVOFunc).collect(Collectors.toList())));
    }

    @Override
    @CacheEvict(value = "RouteList", allEntries = true)
    public void clean() {

    }
}
