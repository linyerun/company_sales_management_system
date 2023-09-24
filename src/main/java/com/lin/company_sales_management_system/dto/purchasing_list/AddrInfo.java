package com.lin.company_sales_management_system.dto.purchasing_list;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "地址信息封装类")
public class AddrInfo {

    @ApiModelProperty(required = true)
    private Long ContractId;

    @ApiModelProperty(required = true, value = "最大长度为100")
    private String address;
}
