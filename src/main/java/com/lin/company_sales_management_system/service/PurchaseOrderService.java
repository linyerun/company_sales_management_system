package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.purchase_order_info.PurchaseOrderInfoData;
import com.lin.company_sales_management_system.entity.PurchaseOrder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {
    void updateStateCount(PurchaseOrderInfoData purchaseOrderInfoData, Long goodsId, Long goodsCount) throws Exception;
}
