package com.lin.company_sales_management_system.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息封装类")
public class UserInfo {
    @ApiModelProperty(value = "长度至少为6",required = true)
    private String username;

    @ApiModelProperty(value = "长度至少为6",required = true)
    private String password;

    @ApiModelProperty(value = "长度为11",required = true)
    private String phoneNumber;

    @ApiModelProperty(required = true)
    private String email;

    @ApiModelProperty(value = "长度在1与20之间",required = true)
    private String employeeName;
}
