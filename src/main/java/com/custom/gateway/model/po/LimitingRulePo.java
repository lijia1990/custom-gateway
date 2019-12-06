package com.custom.gateway.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "limiting_rule")
@EqualsAndHashCode(callSuper = false)
public class LimitingRulePo {
    public LimitingRulePo(Long id, Boolean isDel) {
        this.id = id;
        this.isDel = isDel;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String ruleName;

    private Boolean ruleType;

    private String ruleVal;

    private Long qpsCount;

    private String limitingHz;

    private Long limitingStartTime;

    private Long limitingEndTime;

    private Boolean isDel;

    private Date createTm;

    private Date updateTm;

    private String createId;

    private String updateId;
}
