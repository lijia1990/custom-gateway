package com.custom.gateway.service;

import com.custom.gateway.model.form.LimitingRuleGlobalQueryForm;
import com.custom.gateway.model.po.LimitingRuleGlobalPo;
import com.custom.gateway.model.vo.LimitingRuleGlobalVo;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface CurrentLimitingGlobalService {

    void save(LimitingRuleGlobalPo po);

    void delete(Long id);

    void update(LimitingRuleGlobalPo po);

    LimitingRuleGlobalPo findById(Long id);

    List<LimitingRuleGlobalVo> queryList(LimitingRuleGlobalQueryForm from);

    Mono<Map> queryForGlobal();

    void clean();
}
