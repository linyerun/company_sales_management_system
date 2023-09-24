package com.lin.company_sales_management_system.dto.contract;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "合同搜索信息封装类")
public class ContractSearchData {
    private Long ContractId;    // 根据合同ID
    private Long clientId;      // 根据客户ID
    private Long userId;        // 根据用户ID
}
