package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.dispatch_bill.CourierNumberInfo;
import com.lin.company_sales_management_system.dto.dispatch_bill.DispatchBillInfo;
import com.lin.company_sales_management_system.entity.DispatchBill;

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
public interface DispatchBillService extends IService<DispatchBill> {
    void post(DispatchBillInfo dispatchBillInfo) throws Exception;

    List<DispatchBill> get();

    Map<String, Object> postCourierNumber(CourierNumberInfo info, Long dispatchBillId);
}
