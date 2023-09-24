package com.lin.company_sales_management_system.dto.dispatch_bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "快递单号信息封装类")
public class CourierNumberInfo {

    @ApiModelProperty(required = true)
    private String courierNumber;

    @ApiModelProperty(required = true)
    private String clientEmail;
}
