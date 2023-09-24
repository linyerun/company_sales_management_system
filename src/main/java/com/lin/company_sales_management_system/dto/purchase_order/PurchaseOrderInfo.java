package com.lin.company_sales_management_system.dto.purchase_order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "进货单信息封装类")
public class PurchaseOrderInfo {
    @ApiModelProperty(required = true)
    private Long goodsId;
    @ApiModelProperty(required = true)
    private Long goodsCount;
}
