package com.lin.company_sales_management_system.vo.purchasing_goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasingGoodsData {
    // Goods相关参数
    private Long goodId;
    private String goodsName;
    private BigDecimal unitPrice;
    // 自身相关
    private Long purchasingGoodsId;
    private Long purchasingCount;
}
