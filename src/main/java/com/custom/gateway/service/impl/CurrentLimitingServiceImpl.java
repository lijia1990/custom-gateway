package com.custom.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.custom.gateway.config.AssembleRouteDefinition;
import com.custom.gateway.config.annotation.CacheClean;
import com.custom.gateway.dao.CustomCurrentLimitingMapper;
import com.custom.gateway.model.core.PageBean;
import com.custom.gateway.model.po.LimitingRulePo;
import com.custom.gateway.model.po.RoutePo;
import com.custom.gateway.model.vo.LimitingRuleVo;
import com.custom.gateway.service.CurrentLimitingService;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.custom.gateway.config.AssembleRouteDefinition.instanceInfoToCustomRouteVOFunc;

@Service
@CacheConfig(cacheNames = "currentLimiting")
@Transactional(rollbackFor = RuntimeException.class)
@Import(AssembleRouteDefinition.class)
public class CurrentLimitingServiceImpl implements CurrentLimitingService {
    private static final String CURRENT_LIMITING="currentLimiting";
    @Autowired
    private CustomCurrentLimitingMapper dao;

    @Override
    @CacheClean(CURRENT_LIMITING)
    public void save(LimitingRulePo po) {
        dao.insert(po);
    }

    @Override
    @CacheClean(CURRENT_LIMITING)
    public void delete(Long id) {
        dao.updateById(new LimitingRulePo(id, true));
    }

    @Override
    @CacheClean(CURRENT_LIMITING)
    public void update(LimitingRulePo po) {
        dao.updateById(po);
    }

    @Override
    @Cacheable(value = CURRENT_LIMITING, key = "#id")
    public LimitingRulePo findById(Long id) {
        return null;
    }

    @Override
    public List<LimitingRuleVo> queryList() {
        return null;
    }

    @Override
    @Cacheable(value = CURRENT_LIMITING, key = "#val")
    public Mono<LimitingRuleVo> queryForVal(String val) {
        PageHelper.startPage(0, 1);
        List<LimitingRulePo> list = dao.selectList(new QueryWrapper<LimitingRulePo>()
                .eq("is_del", 0).eq(Strings.isNotBlank(val), "rule_val", val));
        if (list.isEmpty()) {
            return Mono.empty();
        }
        return Mono.just(list.stream().map(instanceInfoToCustomRouteVOFunc).collect(Collectors.toList()).get(0));
    }

    @Override
    public void clean() {

    }
}
