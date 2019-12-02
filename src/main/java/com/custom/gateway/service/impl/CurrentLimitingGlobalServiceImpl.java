package com.custom.gateway.service.impl;

import com.custom.gateway.config.annotation.CacheClean;
import com.custom.gateway.dao.CustomCurrentLimitingGlobalMapper;
import com.custom.gateway.model.po.LimitingRuleGlobalPo;
import com.custom.gateway.model.vo.LimitingRuleGlobalVo;
import com.custom.gateway.service.CurrentLimitingGlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "currentLimitingGlobal")
@Transactional(rollbackFor = RuntimeException.class)
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
    @Cacheable(value = "currentLimitingGlobalList")
    public List<LimitingRuleGlobalVo> queryList() {
        return null;
    }

    @Override
    @CacheEvict(value = "currentLimitingGlobalList", allEntries = true)
    public void clean() {

    }
}
