package com.custom.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.custom.gateway.config.AssembleRouteDefinition;
import com.custom.gateway.config.annotation.CacheClean;
import com.custom.gateway.dao.CustomCurrentLimitingGlobalMapper;
import com.custom.gateway.model.form.LimitingRuleGlobalQueryForm;
import com.custom.gateway.model.po.LimitingRuleGlobalPo;
import com.custom.gateway.model.po.LimitingRulePo;
import com.custom.gateway.model.vo.LimitingRuleGlobalVo;
import com.custom.gateway.service.CurrentLimitingGlobalService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.custom.gateway.config.AssembleRouteDefinition.instanceGlobalInfoToCustomRouteVOFunc;
import static com.custom.gateway.config.AssembleRouteDefinition.instanceInfoToCustomRouteVOFunc;

@Service
@CacheConfig(cacheNames = "currentLimitingGlobal")
@Transactional(rollbackFor = RuntimeException.class)
@Import(AssembleRouteDefinition.class)
public class CurrentLimitingGlobalServiceImpl implements CurrentLimitingGlobalService {
    @Autowired
    private CustomCurrentLimitingGlobalMapper dao;


    @Override
    @CacheClean("currentLimitingGlobal")
    public void save(LimitingRuleGlobalPo po) {
        dao.insert(po);
    }

    @Override
    @CacheClean("currentLimitingGlobal")
    public void delete(Long id) {
        dao.updateById(new LimitingRuleGlobalPo(id, true));
    }

    @Override
    @CacheClean("currentLimitingGlobal")
    public void update(LimitingRuleGlobalPo po) {
        dao.updateById(po);
    }

    @Override
    @Cacheable(value = "currentLimitingGlobal", key = "#id")
    public LimitingRuleGlobalPo findById(Long id) {
        return dao.selectById(id);
    }

    @Override

    public List<LimitingRuleGlobalVo> queryList(LimitingRuleGlobalQueryForm form) {
        return null;
    }

    @Override
    @Cacheable(value = "currentLimitingGlobalList")
    public Mono<LimitingRuleGlobalVo> queryForGlobal() {
        PageHelper.startPage(0, 1);
        List<LimitingRuleGlobalPo> list = dao.selectList(new QueryWrapper<LimitingRuleGlobalPo>().eq("is_del", 0));
        if (list.isEmpty()) {
            return Mono.empty();
        }
        return Mono.just(list.stream().map(instanceGlobalInfoToCustomRouteVOFunc).collect(Collectors.toList()).get(0));
    }

    @Override
    @CacheEvict(value = "currentLimitingGlobalList", allEntries = true)

    public void clean() {

    }
}
