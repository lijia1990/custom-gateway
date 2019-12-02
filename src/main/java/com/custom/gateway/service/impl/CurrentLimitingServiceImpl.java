package com.custom.gateway.service.impl;

import com.custom.gateway.config.annotation.CacheClean;
import com.custom.gateway.dao.CustomCurrentLimitingMapper;
import com.custom.gateway.model.po.LimitingRulePo;
import com.custom.gateway.model.vo.LimitingRuleVo;
import com.custom.gateway.service.CurrentLimitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "currentLimiting")
@Transactional(rollbackFor = RuntimeException.class)
public class CurrentLimitingServiceImpl implements CurrentLimitingService {
    @Autowired
    private CustomCurrentLimitingMapper dao;

    @Override
    @CacheClean("currentLimiting")
    public void save(LimitingRulePo po) {
        dao.insert(po);
    }

    @Override
    @CacheClean("currentLimiting")
    public void delete(Long id) {
        dao.updateById(new LimitingRulePo(id, true));
    }

    @Override
    @CacheClean("currentLimiting")
    public void update(LimitingRulePo po) {
        dao.updateById(po);
    }

    @Override
    @Cacheable(value = "currentLimiting", key = "#id")
    public LimitingRulePo findById(Long id) {
        return null;
    }

    @Override
    public List<LimitingRuleVo> queryList() {
        return null;
    }

    @Override
    public void clean() {

    }
}
