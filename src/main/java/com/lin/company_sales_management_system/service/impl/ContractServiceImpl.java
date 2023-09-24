package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.contract.ClientInfo;
import com.lin.company_sales_management_system.dto.contract.ContractInfo;
import com.lin.company_sales_management_system.dto.purchasing_goods.PurchasingGoodsInfo;
import com.lin.company_sales_management_system.entity.*;
import com.lin.company_sales_management_system.mapper.*;
import com.lin.company_sales_management_system.service.ContractService;
import com.lin.company_sales_management_system.utils.EmailUtil;
import com.lin.company_sales_management_system.vo.contract.ContractState;
import com.lin.company_sales_management_system.vo.contract.State;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final PurchasingListMapper purchasingListMapper;
    private final PurchasingGoodsMapper purchasingGoodsMapper;

    private final EmailUtil emailUtil;

    @Value("${client.pay.page-base-url}")
    private String baseURL;

    public ContractServiceImpl(UserMapper userMapper, ClientMapper clientMapper, PurchasingListMapper purchasingListMapper, PurchasingGoodsMapper purchasingGoodsMapper, EmailUtil emailUtil) {
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
        this.purchasingListMapper = purchasingListMapper;
        this.purchasingGoodsMapper = purchasingGoodsMapper;
        this.emailUtil = emailUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> record(ContractInfo contractInfo) throws Exception {
        Map<String, Object> res = new HashMap<>();

        // 返回是否存在这个客户、销售人员
        User user = userMapper.selectById(contractInfo.getUserId());
        if (Objects.isNull(user)) {
            res.put("code", ResultCode.PARAM_NOT_VALID.getCode());
            res.put("err", "所选择的销售人员不存在");
            return res;
        }
        Client client = clientMapper.selectById(contractInfo.getClientId());
        if (Objects.isNull(client)) {
            res.put("code", ResultCode.PARAM_NOT_VALID.getCode());
            res.put("err", "所选择的客户不存在");
            return res;
        }

        // 保存合同
        Contract contract = new Contract();
        contract.setClientId(contractInfo.getClientId());
        contract.setUserId(contractInfo.getUserId());
        contract.setTotalAmount(contractInfo.getTotalAmount());
        contract.setContractPic(contractInfo.getContractPic());
        contract.setContractState((byte) 0); //表示还没付款

        int returnValue = baseMapper.insert(contract);
        if (returnValue != 1) {
            throw new Exception("500;系统繁忙,合同保存失败!");
        }

        // 创建采购清单
        PurchasingList purchasingList = new PurchasingList();
        purchasingList.setContractId(contract.getId());
        returnValue = purchasingListMapper.insert(purchasingList);
        if (returnValue != 1) {
            throw new Exception("500;系统繁忙,采购清单保存失败!");
        }

        // 保存清单货物
        List<PurchasingGoods> list = new ArrayList<>();
        for (PurchasingGoodsInfo goods : contractInfo.getGoodsInfos()) {
            PurchasingGoods purchasingGoods = new PurchasingGoods();
            // 加入集合当中
            list.add(purchasingGoods);
            // 设置值
            purchasingGoods.setPurchasingCount(goods.getPurchasingCount());
            purchasingGoods.setPurchasingListId(purchasingList.getId());
            purchasingGoods.setGoodsId(goods.getGoodsId());
            purchasingGoods.setState((byte) 0);
            purchasingGoods.setCreatedAt(LocalDateTime.now());
            purchasingGoods.setUpdatedAt(purchasingGoods.getCreatedAt());
        }

        returnValue = purchasingGoodsMapper.insertBatch(list);
        if (returnValue != list.size()) {
            throw new Exception("500;系统繁忙,采购清单货物保存失败!");
        }

        String url = baseURL + contract.getId().toString();
        String content = "请点击此链接进行付款操作: " + url;
        emailUtil.sendEmail("付款", content, client.getEmail());

        return null;
    }

    @Override
    public BigDecimal getSumByTime(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Contract::getCreatedAt, startTime).le(Contract::getUpdatedAt, endTime);
        List<Contract> contracts = baseMapper.selectList(queryWrapper);
        BigDecimal sum = new BigDecimal("0");
        for (Contract contract : contracts) {
            sum = sum.add(contract.getTotalAmount());
        }
        return sum;
    }

    @Override
    public BigDecimal getSumByClientId(Long clientId) {

        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contract::getClientId, clientId);

        List<Contract> contracts = baseMapper.selectList(queryWrapper);

        BigDecimal sum = new BigDecimal("0");
        for (Contract contract : contracts) {
            sum = sum.add(contract.getTotalAmount());
        }

        return sum;
    }

    @Override
    public List<State> getContractStatesByEmployeeId(Long employeeId) {
        // 判断一下这个销售员工是否存在
        User user = userMapper.selectById(employeeId);
        if (Objects.isNull(user) || user.getIdentity() != 0) return null;

        // 查询合同
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contract::getUserId, employeeId);

        List<Contract> contracts = baseMapper.selectList(queryWrapper);

        List<State> stateList = new ArrayList<>();

        for (Contract contract : contracts) {
            Byte code = contract.getContractState();
            State state = new State(contract.getId(), code, ContractState.getMsgByCode(code));
            stateList.add(state);
        }

        return stateList;
    }

    @Override
    public BigDecimal getEmployeeSumByTime(Long employeeId, LocalDateTime startTime, LocalDateTime endTime) {
        // 直接查吧，不存在这个销售员工就直接返回0就行了
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .ge(Contract::getCreatedAt, startTime)
                .le(Contract::getUpdatedAt, endTime)
                .eq(Contract::getUserId, employeeId);

        BigDecimal sum = new BigDecimal("0");

        List<Contract> contracts = baseMapper.selectList(queryWrapper);
        for (Contract contract : contracts) {
            sum = sum.add(contract.getTotalAmount());
        }

        return sum;
    }

    @Override
    public ClientInfo getClientInfo(Long contractId) {
        Long purchasingListId = purchasingListMapper.selectIdByContractId(contractId);

        // 获取发货地址
        PurchasingList purchasingList = purchasingListMapper.selectById(purchasingListId);

        // 获取客户信息
        Contract contract = baseMapper.selectById(contractId);
        Client client = clientMapper.selectById(contract.getClientId());

        // 封装返回信息
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setAddress(purchasingList.getAddress());
        clientInfo.setEmail(client.getEmail());
        clientInfo.setClientName(client.getClientName());
        clientInfo.setPhoneNumber(client.getPhoneNumber());

        return clientInfo;
    }

}
