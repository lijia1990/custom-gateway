package com.custom.gateway.service;

import com.custom.gateway.model.po.LimitingRuleGlobalPo;
import com.custom.gateway.model.po.LimitingRulePo;
import com.custom.gateway.model.po.RoutePo;
import com.custom.gateway.model.vo.LimitingRuleGlobalVo;
import com.custom.gateway.model.vo.LimitingRuleVo;

import java.util.List;

public interface CurrentLimitingGlobalService {

    void save(LimitingRuleGlobalPo po);

    void delete(Long id);

    void update(LimitingRuleGlobalPo po);

    LimitingRuleGlobalPo findById(Long id);

    List<LimitingRuleGlobalVo> queryList();

    void clean();
}
