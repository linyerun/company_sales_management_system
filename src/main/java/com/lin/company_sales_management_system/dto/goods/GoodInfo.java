package com.lin.company_sales_management_system.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "仓库货物信息封装类")
public class GoodInfo {

    @ApiModelProperty(required = true)
    private String goodsName;

    @ApiModelProperty(required = true)
    private Long goodsCount;

    @ApiModelProperty(required = true)
    private BigDecimal unitPrice;
}
