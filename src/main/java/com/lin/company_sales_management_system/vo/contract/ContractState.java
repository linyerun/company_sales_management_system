package com.lin.company_sales_management_system.vo.contract;

import org.springframework.security.core.parameters.P;

public enum ContractState {
    UNPAID((byte)0, "客户尚未付款"),
    PAY((byte)1, "已经付款,正在发货中"),
    END((byte)2, "完成所有发货,合同履行结束");

    private Byte code;
    private String msg;

    ContractState(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Byte getCode() {
        return code;
    }

    public static String getMsgByCode(Byte code) {
        if (UNPAID.code == code) return UNPAID.msg;
        else if (PAY.code == code) return PAY.msg;
        return END.msg;
    }

}
