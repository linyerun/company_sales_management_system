package com.lin.company_sales_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.company_sales_management_system.entity.PurchasingGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Mapper
public interface PurchasingGoodsMapper extends BaseMapper<PurchasingGoods> {
    int insertBatch(@Param("list") List<PurchasingGoods> goodsList);
}
