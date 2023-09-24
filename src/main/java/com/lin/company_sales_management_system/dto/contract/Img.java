package com.lin.company_sales_management_system.dto.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "图片信息封装类")
public class Img {
    @ApiModelProperty(required = true)
    private String url;
}
