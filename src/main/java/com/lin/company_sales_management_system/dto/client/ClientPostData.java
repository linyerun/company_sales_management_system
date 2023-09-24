package com.lin.company_sales_management_system.dto.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "客户信息封装类")
public class ClientPostData {

    @ApiModelProperty(required = true)
    private String phoneNumber;

    @ApiModelProperty(required = true)
    private String email;

    @ApiModelProperty(required = true)
    private String clientName;
}
