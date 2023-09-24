package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.dto.goods.GoodInfo;
import com.lin.company_sales_management_system.entity.Goods;
import com.lin.company_sales_management_system.entity.PurchasingGoods;
import com.lin.company_sales_management_system.mapper.GoodsMapper;
import com.lin.company_sales_management_system.mapper.PurchasingGoodsMapper;
import com.lin.company_sales_management_system.service.GoodsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final PurchasingGoodsMapper purchasingGoodsMapper;

    public GoodsServiceImpl(PurchasingGoodsMapper purchasingGoodsMapper) {
        this.purchasingGoodsMapper = purchasingGoodsMapper;
    }

    @Override
    public BigDecimal getSumByGoodsId(Long goodsId) {

        // 查询该货物的单价
        Goods goods = baseMapper.selectById(goodsId);
        if (Objects.isNull(goods)) return null; // 不存在则返回空

        BigDecimal sum = new BigDecimal("0");

        LambdaQueryWrapper<PurchasingGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchasingGoods::getGoodsId, goodsId);

        List<PurchasingGoods> purchasingGoods = purchasingGoodsMapper.selectList(queryWrapper);

        for (PurchasingGoods pg : purchasingGoods) {
            sum = sum.add(goods.getUnitPrice().multiply(new BigDecimal(pg.getPurchasingCount())));
        }

        return sum;
    }

    @Override
    public Map<String, Object> post(GoodInfo goodInfo) {
        Goods goods = new Goods();
        goods.setGoodsCount(goodInfo.getGoodsCount());
        goods.setGoodsName(goodInfo.getGoodsName());
        goods.setUnitPrice(goodInfo.getUnitPrice());

        int returnValue = baseMapper.insert(goods);

        if (returnValue != 1) {
            return new HashMap<String, Object>(){{
                put("code", 500);
                put("err", "系统繁忙,添加失败,请稍后再进行尝试!");
            }};
        }

        return null;
    }
}
