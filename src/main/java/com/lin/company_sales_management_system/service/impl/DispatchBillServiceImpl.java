package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.dto.dispatch_bill.CourierNumberInfo;
import com.lin.company_sales_management_system.dto.dispatch_bill.DispatchBillInfo;
import com.lin.company_sales_management_system.entity.Contract;
import com.lin.company_sales_management_system.entity.DispatchBill;
import com.lin.company_sales_management_system.entity.Goods;
import com.lin.company_sales_management_system.entity.PurchasingGoods;
import com.lin.company_sales_management_system.mapper.*;
import com.lin.company_sales_management_system.service.DispatchBillService;
import com.lin.company_sales_management_system.utils.EmailUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Service
public class DispatchBillServiceImpl extends ServiceImpl<DispatchBillMapper, DispatchBill> implements DispatchBillService {

    private final PurchasingGoodsMapper pgMapper;

    private final PurchasingListMapper plMapper;

    private final ContractMapper contractMapper;

    private final EmailUtil emailUtil;

    private final GoodsMapper goodsMapper;

    public DispatchBillServiceImpl(PurchasingGoodsMapper pgMapper, PurchasingListMapper plMapper, ContractMapper contractMapper, EmailUtil emailUtil, GoodsMapper goodsMapper) {
        this.pgMapper = pgMapper;
        this.plMapper = plMapper;
        this.contractMapper = contractMapper;
        this.emailUtil = emailUtil;
        this.goodsMapper = goodsMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void post(DispatchBillInfo info) throws Exception {
        // 查看货物库存
        Goods goods = goodsMapper.selectById(info.getGoodsId());
        if (goods.getGoodsCount().compareTo(info.getGoodsCount()) < 0) {
            // 返回信息中提醒用户库存剩余
            throw new Exception("500;库存不够，生成发货单失败，库存还剩: "+goods.getGoodsCount()+"。");
        }

        // 修改货物库存
        Goods g = new Goods();
        g.setId(goods.getId());
        g.setGoodsCount(goods.getGoodsCount() - info.getGoodsCount());
        int returnValue = goodsMapper.updateById(g);
        if (returnValue != 1) {
            throw new Exception("500;修改库存数量失败");
        }

        // 生成发货单
        DispatchBill dispatchBill = new DispatchBill();
        dispatchBill.setContractId(info.getContractId());
        dispatchBill.setGoodsId(info.getGoodsId());
        dispatchBill.setGoodsCount(info.getGoodsCount());
        returnValue = baseMapper.insert(dispatchBill);
        if (returnValue != 1) {
            throw new Exception("500;生成发货单失败");
        }

        // 改变货物状态(根据货物ID来)
        PurchasingGoods pg = new PurchasingGoods();
        pg.setId(info.getPurchasingGoodsId());
        pg.setState((byte) 1);
        returnValue = pgMapper.updateById(pg);
        if (returnValue != 1) {
            throw new Exception("500;改变货物状态失败");
        }
        // 判断货物是否全部发完
        // 看还有无处于0状态的
        Long purchasingListId = plMapper.selectIdByContractId(info.getContractId());
        LambdaQueryWrapper<PurchasingGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchasingGoods::getState, 0);
        queryWrapper.eq(PurchasingGoods::getPurchasingListId, purchasingListId);
        Integer count = pgMapper.selectCount(queryWrapper);

        // 根据判断更新状态
        if (count.equals(0)) {
            // 更新合同状态
            Contract contract = new Contract();
            contract.setId(info.getContractId());
            contract.setContractState((byte) 2);
            returnValue = contractMapper.updateById(contract);
            if (returnValue != 1) {
                throw new Exception("500;更新合同状态失败");
            }
        }
    }

    @Override
    public List<DispatchBill> get() {
        // 快递单号还没填，一看就是为处理的了。
        return baseMapper
                .selectList(new LambdaQueryWrapper<DispatchBill>()
                        .isNull(DispatchBill::getCourierNumber)
                );
    }

    @Override
    public Map<String, Object> postCourierNumber(CourierNumberInfo info, Long dispatchBillId) {
        DispatchBill dispatchBill = new DispatchBill();
        dispatchBill.setId(dispatchBillId);
        dispatchBill.setCourierNumber(info.getCourierNumber());

        int returnValue = baseMapper.updateById(dispatchBill);

        if (returnValue != 1) {
            return new HashMap<String, Object>() {{
                put("err", "系统繁忙，保存");
                put("code", 500);
            }};
        }

        emailUtil.sendEmail("你的一个货物已发货", "货物详细信息请查看此快递单号: " + info.getCourierNumber(), info.getClientEmail());

        return null;
    }

}
