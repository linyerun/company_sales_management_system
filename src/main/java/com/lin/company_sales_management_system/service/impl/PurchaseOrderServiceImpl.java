package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.dto.purchase_order_info.PurchaseOrderInfoData;
import com.lin.company_sales_management_system.entity.Goods;
import com.lin.company_sales_management_system.entity.PurchaseOrder;
import com.lin.company_sales_management_system.entity.PurchaseOrderInfo;
import com.lin.company_sales_management_system.mapper.GoodsMapper;
import com.lin.company_sales_management_system.mapper.PurchaseOrderInfoMapper;
import com.lin.company_sales_management_system.mapper.PurchaseOrderMapper;
import com.lin.company_sales_management_system.service.PurchaseOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    private final GoodsMapper goodsMapper;
    private final PurchaseOrderInfoMapper poiMapper;

    public PurchaseOrderServiceImpl(GoodsMapper goodsMapper, PurchaseOrderInfoMapper poiMapper) {
        this.goodsMapper = goodsMapper;
        this.poiMapper = poiMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStateCount(PurchaseOrderInfoData purchaseOrderInfoData, Long goodsId, Long goodsCount) throws Exception {

        // 提交进货信息
        PurchaseOrderInfo info = new PurchaseOrderInfo();
        info.setCommentInfo(purchaseOrderInfoData.getCommentInfo());
        info.setPurchaseOrderId(purchaseOrderInfoData.getPurchaseOrderId());
        info.setUnitPrice(purchaseOrderInfoData.getUnitPrice());
        int returnValue = poiMapper.insert(info);
        if (returnValue != 1) {
            throw new Exception("500;提交进货单进货信息失败!");
        }

        // 更新进货单状态
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setState((byte)1);
        purchaseOrder.setId(purchaseOrderInfoData.getPurchaseOrderId());
        returnValue = baseMapper.updateById(purchaseOrder);
        if (returnValue != 1) {
            throw new Exception("500;更新进货单状态失败!");
        }

        // 更新仓库库存
        Goods goods = goodsMapper.selectById(goodsId);
        Goods g = new Goods();
        g.setId(goodsId);
        g.setGoodsCount(goodsCount + goods.getGoodsCount());

        returnValue = goodsMapper.updateById(g);
        if (returnValue != 1) {
            throw new Exception("500;更新货物库存失败!");
        }
    }
}
