package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.contract.ClientInfo;
import com.lin.company_sales_management_system.dto.contract.ContractInfo;
import com.lin.company_sales_management_system.entity.Contract;
import com.lin.company_sales_management_system.vo.contract.State;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
public interface ContractService extends IService<Contract> {
    Map<String, Object> record(ContractInfo contractInfo) throws Exception;

    BigDecimal getSumByTime(LocalDateTime startTime, LocalDateTime endTime);

    BigDecimal getSumByClientId(Long clientId);

    List<State> getContractStatesByEmployeeId(Long employeeId);

    BigDecimal getEmployeeSumByTime(Long employeeId, LocalDateTime startTime, LocalDateTime endTime);

    ClientInfo getClientInfo(Long contractId);
}
