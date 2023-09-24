package com.lin.company_sales_management_system.dto.purchase_order_info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "进货单进货信息封装类")
public class PurchaseOrderInfoData {

    @ApiModelProperty(required = true)
    private Long purchaseOrderId;

    @ApiModelProperty(required = true)
    private BigDecimal unitPrice;

    @ApiModelProperty(required = true)
    private String commentInfo;
}
