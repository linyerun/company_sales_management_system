package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.purchase_order_info.PurchaseOrderInfoData;
import com.lin.company_sales_management_system.entity.PurchaseOrderInfo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-12-03
 */
public interface PurchaseOrderInfoService extends IService<PurchaseOrderInfo> {

    Map<String, Object> postInfo(PurchaseOrderInfoData purchaseOrderInfoData);
}
