package com.custom.gateway.model.core;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@Api(value = "查询顶级类", tags = {"查询顶级类"})
public class BaseQueryForm implements Serializable {

    @Min(value = 1, message = "当前页数必须大于1的整数")
    @ApiModelProperty(value = "当前页数", required = true)
    private Integer pageNo;

    @Min(value = 1, message = "数据分页数必须大于1的整数")
    @ApiModelProperty(value = "数据分页数", required = true)
    private Integer pageSize;
}
