package com.lin.company_sales_management_system.dto.purchasing_goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "清单商品信息封装类")
public class PurchasingGoodsInfo {
    @ApiModelProperty(required = true)
    private Long GoodsId;
    @ApiModelProperty(required = true)
    private Long PurchasingCount;
}
