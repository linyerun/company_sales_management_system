package com.lin.company_sales_management_system.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserMsg extends UserInfo{
    @ApiModelProperty(required = true)
    private Long id;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String employeeName;
}
