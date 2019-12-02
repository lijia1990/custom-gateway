package com.custom.gateway.service;

import com.custom.gateway.model.po.LimitingRulePo;
import com.custom.gateway.model.vo.LimitingRuleVo;

import java.util.List;

public interface CurrentLimitingService {

    void save(LimitingRulePo po);

    void delete(Long id);

    void update(LimitingRulePo po);

    LimitingRulePo findById(Long id);

    List<LimitingRuleVo> queryList();

    void clean();
}
