package com.custom.gateway.model.vo;

import com.custom.gateway.model.core.LimitingVo;
import lombok.Data;

import java.util.Date;

@Data
public class LimitingRuleVo extends LimitingVo {

    private Long id;

    private String ruleName;

    private Boolean ruleType;

    private String ruleVal;

    private Long qpsCount;

    private String limitingHz;

    private Boolean isDel;

    private Date createTm;

    private Date updateTm;

    private String createId;

    private String updateId;
}
