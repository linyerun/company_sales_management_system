package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.dto.purchasing_list.AddrInfo;
import com.lin.company_sales_management_system.entity.Contract;
import com.lin.company_sales_management_system.entity.PurchasingList;
import com.lin.company_sales_management_system.mapper.ContractMapper;
import com.lin.company_sales_management_system.mapper.PurchasingListMapper;
import com.lin.company_sales_management_system.service.PurchasingListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Service
public class PurchasingListServiceImpl extends ServiceImpl<PurchasingListMapper, PurchasingList> implements PurchasingListService {

    private final ContractMapper contractMapper;

    public PurchasingListServiceImpl(ContractMapper contractMapper) {
        this.contractMapper = contractMapper;
    }

    @Override
    public List<PurchasingList> getPurchasingListByState(Byte state) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contract::getContractState, state);
        List<Contract> contracts = contractMapper.selectList(queryWrapper);
        List<PurchasingList> purchasingLists = new ArrayList<>();
        for (Contract contract : contracts) {
            PurchasingList p = baseMapper
                    .selectOne(new LambdaQueryWrapper<PurchasingList>()
                            .eq(PurchasingList::getContractId, contract.getId()));

            purchasingLists.add(p);
        }
        return purchasingLists;
    }

    @Override
    public Long selectIdByContractId(Long contractId) {
        return baseMapper.selectIdByContractId(contractId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postAddress(AddrInfo addrInfo) throws Exception {
        // 根据合同ID查询订单ID
        Long purchasingListId = baseMapper.selectIdByContractId(addrInfo.getContractId());

        // 给订单填写地址
        PurchasingList purchasingList = new PurchasingList();
        purchasingList.setId(purchasingListId);
        purchasingList.setAddress(addrInfo.getAddress());
        int returnValue = baseMapper.updateById(purchasingList);
        if (returnValue != 1) {
            throw new Exception("500;系统繁忙，填写地址失败，请稍后再进行尝试！");
        }

        // 改变合同的状态
        Contract contract = new Contract();
        contract.setId(addrInfo.getContractId());
        contract.setContractState((byte)1);
        returnValue = contractMapper.updateById(contract);
        if (returnValue != 1) {
            throw new Exception("500;系统繁忙，修改合同状态失败，请稍后再进行尝试！");
        }
    }
}
