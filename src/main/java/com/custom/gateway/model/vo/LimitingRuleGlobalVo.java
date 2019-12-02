package com.custom.gateway.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
public class LimitingRuleGlobalVo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String ruleName;

    private Boolean ruleType;

    private Long qpsCount;

    private Long limitingStartTime;

    private Long limitingEndTime;

    private Boolean isDel;

    private Date createTm;

    private Date updateTm;

    private String createId;

    private String updateId;
}
