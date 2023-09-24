package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.purchasing_list.AddrInfo;
import com.lin.company_sales_management_system.entity.PurchasingList;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
public interface PurchasingListService extends IService<PurchasingList> {
    Byte UNPAID = 0;
    Byte ING = 1;
    Byte End = 2;
    List<PurchasingList> getPurchasingListByState(Byte state);

    Long selectIdByContractId(Long contractId);

    void postAddress(AddrInfo addrInfo) throws Exception;
}
