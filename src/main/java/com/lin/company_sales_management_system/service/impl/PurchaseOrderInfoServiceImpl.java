package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.dto.purchase_order_info.PurchaseOrderInfoData;
import com.lin.company_sales_management_system.entity.PurchaseOrderInfo;
import com.lin.company_sales_management_system.mapper.PurchaseOrderInfoMapper;
import com.lin.company_sales_management_system.service.PurchaseOrderInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-12-03
 */
@Service
public class PurchaseOrderInfoServiceImpl extends ServiceImpl<PurchaseOrderInfoMapper, PurchaseOrderInfo> implements PurchaseOrderInfoService {

    @Override
    public Map<String, Object> postInfo(PurchaseOrderInfoData purchaseOrderInfoData) {
        PurchaseOrderInfo info = new PurchaseOrderInfo();
        info.setCommentInfo(purchaseOrderInfoData.getCommentInfo());
        info.setPurchaseOrderId(purchaseOrderInfoData.getPurchaseOrderId());
        info.setUnitPrice(purchaseOrderInfoData.getUnitPrice());
        int returnValue = baseMapper.insert(info);
        if (returnValue != 1) {
            return new HashMap<String, Object>(){{
                put("code", 500);
                put("err", "系统异常, 插入数据失败, 请稍后再进行尝试!");
            }};
        }
        return null;
    }
}
