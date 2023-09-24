package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.goods.GoodInfo;
import com.lin.company_sales_management_system.entity.Goods;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
public interface GoodsService extends IService<Goods> {

    BigDecimal getSumByGoodsId(Long goodsId);

    Map<String, Object> post(GoodInfo goodInfo);
}
