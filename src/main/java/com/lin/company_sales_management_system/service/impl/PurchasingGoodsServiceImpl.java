package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.dto.purchasing_goods.PurchasingGoodsInfo;
import com.lin.company_sales_management_system.entity.Contract;
import com.lin.company_sales_management_system.entity.Goods;
import com.lin.company_sales_management_system.entity.PurchasingGoods;
import com.lin.company_sales_management_system.entity.PurchasingList;
import com.lin.company_sales_management_system.mapper.ContractMapper;
import com.lin.company_sales_management_system.mapper.GoodsMapper;
import com.lin.company_sales_management_system.mapper.PurchasingGoodsMapper;
import com.lin.company_sales_management_system.mapper.PurchasingListMapper;
import com.lin.company_sales_management_system.service.PurchasingGoodsService;
import com.lin.company_sales_management_system.vo.purchasing_goods.PurchasingGoodsData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Service
public class PurchasingGoodsServiceImpl extends ServiceImpl<PurchasingGoodsMapper, PurchasingGoods> implements PurchasingGoodsService {

    private final GoodsMapper goodsMapper;
    private final ContractMapper contractMapper;
    private final PurchasingListMapper purchasingListMapper;

    @Value("${client.pay.page-base-url}")
    private String baseURL;

    public PurchasingGoodsServiceImpl(GoodsMapper goodsMapper, ContractMapper contractMapper, PurchasingListMapper purchasingListMapper) {
        this.goodsMapper = goodsMapper;
        this.contractMapper = contractMapper;
        this.purchasingListMapper = purchasingListMapper;
    }

    @Override
    public List<PurchasingGoodsData> getPurchasingGoodsByPurchasingListId(Long purchasingListId) {
        List<PurchasingGoodsData> infoList = new ArrayList<>();

        LambdaQueryWrapper<PurchasingGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchasingGoods::getPurchasingListId, purchasingListId);
        queryWrapper.eq(PurchasingGoods::getState, 0);

        List<PurchasingGoods> purchasingGoods = baseMapper.selectList(queryWrapper);

        for (PurchasingGoods pg : purchasingGoods) {
            Goods goods = goodsMapper.selectById(pg.getGoodsId());
            PurchasingGoodsData pgd = new PurchasingGoodsData(goods.getId(), goods.getGoodsName(), goods.getUnitPrice(), pg.getId(), pg.getPurchasingCount());
            infoList.add(pgd);
        }

        return infoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long contractId, Long pgId) throws Exception {
        PurchasingGoods purchasingGoods = baseMapper.selectById(pgId);
        if (Objects.isNull(purchasingGoods)) {
            throw new Exception("400;商品ID有误");
        }
        // 删除采购商品
        int val = baseMapper.deleteById(pgId);
        if (val != 1) {
            throw new Exception("500;删除失败");
        }
        // 更新合同总金额
        Contract contract = contractMapper.selectById(contractId);
        if (Objects.isNull(contract)) {
            throw new Exception("400;合同ID有误");
        }
        Goods goods = goodsMapper.selectById(purchasingGoods.getGoodsId());
        Contract c = new Contract();
        c.setId(contractId);
        c.setTotalAmount(contract.getTotalAmount().subtract(new BigDecimal(purchasingGoods.getPurchasingCount()).multiply(goods.getUnitPrice())));
        val = contractMapper.updateById(c);
        if (val != 1) {
            throw new Exception("500;删除失败");
        }
        // 发送付款信息给客户
        // 其实不用了, 毕竟客户上去了, 信息会自动更新

        // Client client = clientMapper.selectById(contract.getClientId());
        // String url = baseURL + contract.getId().toString();
        // String content = "修改合同成功，请点击此链接重新进行付款操作: " + url;
        // emailUtil.sendEmail("付款", content, client.getEmail());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPurchasingGoods(PurchasingGoodsInfo purchasingGoodsInfo, Long contractId) throws Exception {
        Contract contract = contractMapper.selectById(contractId); // 合同
        if (Objects.isNull(contract)) {
            throw new Exception("400;合同ID有误");
        }
        PurchasingList purchasingList = purchasingListMapper.selectOne(new LambdaQueryWrapper<PurchasingList>()
                .eq(PurchasingList::getContractId, contractId));  // 清单
        // 设置购买物品信息
        PurchasingGoods purchasingGoods = new PurchasingGoods();
        purchasingGoods.setPurchasingListId(purchasingList.getId());//清单ID
        purchasingGoods.setGoodsId(purchasingGoodsInfo.getGoodsId());//货物ID
        purchasingGoods.setPurchasingCount(purchasingGoodsInfo.getPurchasingCount());//货物数量
        purchasingGoods.setState((byte)0);// 采购货物状态
        int val = baseMapper.insert(purchasingGoods);
        if (val != 1) {
            throw new Exception("500;保存货物信息失败");
        }
        // 更新合同总金额
        Goods goods = goodsMapper.selectById(purchasingGoods.getGoodsId());
        Contract c = new Contract();
        c.setId(contractId);
        c.setTotalAmount(contract.getTotalAmount().add(new BigDecimal(purchasingGoods.getPurchasingCount()).multiply(goods.getUnitPrice())));
        val = contractMapper.updateById(c);
        if (val != 1) {
            throw new Exception("500;删除失败");
        }
    }
}
