package com.lin.company_sales_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.company_sales_management_system.entity.PurchasingList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Mapper
public interface PurchasingListMapper extends BaseMapper<PurchasingList> {
    Long selectIdByContractId(@Param("contractId") Long contractId);
}
