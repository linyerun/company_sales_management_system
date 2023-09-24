package com.lin.company_sales_management_system.vo.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class State {
    // 合同的ID
    private Long contractId;

    // 0: 客户尚未付款, 1: 已经付款,正在发货中, 2: 完成所有发货,合同履行结束
    private Byte contractState;

    private String msg;
}
