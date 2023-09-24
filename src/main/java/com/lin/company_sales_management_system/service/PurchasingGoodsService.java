package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.purchasing_goods.PurchasingGoodsInfo;
import com.lin.company_sales_management_system.entity.PurchasingGoods;
import com.lin.company_sales_management_system.vo.purchasing_goods.PurchasingGoodsData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
public interface PurchasingGoodsService extends IService<PurchasingGoods> {
    List<PurchasingGoodsData> getPurchasingGoodsByPurchasingListId(Long purchasingListId);
    void deleteByIds(Long contractId, Long pgId) throws Exception;
    void addPurchasingGoods(PurchasingGoodsInfo purchasingGoodsInfo, Long contractId) throws Exception;
}
