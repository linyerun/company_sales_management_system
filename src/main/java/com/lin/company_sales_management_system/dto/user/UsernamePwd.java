package com.lin.company_sales_management_system.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户账号密码封装类")
public class UsernamePwd {

    @ApiModelProperty(value = "长度至少为6",required = true)
    private String username;

    @ApiModelProperty(value = "长度至少为6",required = true)
    private String password;
}
