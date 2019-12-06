package com.custom.gateway.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.custom.gateway.model.core.LimitingVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
public class LimitingRuleGlobalVo extends LimitingVo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String ruleName;

    private Boolean limitingType;

    private Long qpsCount;

    private String limitingHz;

    private Boolean isDel;

    private Date createTm;

    private Date updateTm;

    private String createId;

    private String updateId;
}
