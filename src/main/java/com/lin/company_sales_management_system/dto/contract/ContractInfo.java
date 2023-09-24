package com.lin.company_sales_management_system.dto.contract;

import com.lin.company_sales_management_system.dto.purchasing_goods.PurchasingGoodsInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "合同信息封装类")
public class ContractInfo {

    @ApiModelProperty(required = true)
    private Long clientId;

    @ApiModelProperty(required = true)
    private Long userId;

    @ApiModelProperty(required = true)
    private BigDecimal totalAmount;

    @ApiModelProperty(required = true)
    private String contractPic;

    @ApiModelProperty(required = true)
    private List<PurchasingGoodsInfo> goodsInfos;
}
