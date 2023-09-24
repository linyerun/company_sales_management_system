package com.lin.company_sales_management_system.dto.dispatch_bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "发货单信息封装类")
public class DispatchBillInfo {
    @ApiModelProperty(required = true)
    private Long contractId;
    @ApiModelProperty(required = true)
    private Long goodsId;
    @ApiModelProperty(required = true)
    private Long purchasingGoodsId;
    @ApiModelProperty(required = true)
    private Long goodsCount;
}
