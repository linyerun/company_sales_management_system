package com.lin.company_sales_management_system.dto.contract;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "客户信息封装类")
public class ClientInfo {
    private String address;
    private String email;
    private String clientName;
    private String phoneNumber;
}
