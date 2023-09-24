package com.lin.company_sales_management_system.controller;

import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.dispatch_bill.CourierNumberInfo;
import com.lin.company_sales_management_system.dto.dispatch_bill.DispatchBillInfo;
import com.lin.company_sales_management_system.entity.DispatchBill;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.DispatchBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@RestController
@RequestMapping("/company_sales_management_system/dispatchBill")
@Api(tags = "发货单模块")
public class DispatchBillController {

    private final DispatchBillService dbService;

    public DispatchBillController(DispatchBillService dbService) {
        this.dbService = dbService;
    }

    // 生成发货单，改变货物状态，一定条件下改变合同状态
    @PostMapping("/post")
    @ApiOperation(value = "生成发货单(2)")
    public Result<Object> post(@RequestBody DispatchBillInfo dispatchBillInfo, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 参数校验
        if
        (
                Objects.isNull(dispatchBillInfo) ||
                Objects.isNull(dispatchBillInfo.getContractId()) ||
                Objects.isNull(dispatchBillInfo.getGoodsId()) ||
                Objects.isNull(dispatchBillInfo.getGoodsCount())
        )
            return new Result<>(1001, "参数不能为空值");

        // 出现错误了
        try {
            dbService.post(dispatchBillInfo);
        } catch (Exception e) {
            String[] s = e.getMessage().split(";");
            String msg = s[1];
            int code = Integer.parseInt(s[0]);
            return new Result<>(code, msg);
        }

        // 成功提交了
        return new Result<>();
    }

    // 仓库管理员获取待处理发货单
    @GetMapping("/get")
    @ApiOperation(value = "获取待处理发货单(12)")
    public Result<List<DispatchBill>> get(HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        if (user.getIdentity().equals((byte) 0))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        List<DispatchBill> dispatchBills = dbService.get();

        return new Result<>(dispatchBills);
    }

    // 仓库管理员填写快递单号
    @PostMapping("/postCourierNumber/{id}")
    @ApiOperation(value = "仓库管理员填写快递单号(1)")
    public Result<Object> postCourierNumber(@PathVariable("id") Long id,@RequestBody CourierNumberInfo info, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 1))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        Map<String, Object> res = dbService.postCourierNumber(info,id);
        if (!Objects.isNull(res)) {
            String msg = (String) res.get("err");
            Integer code = (Integer) res.get("code");
            return new Result<>(code, msg);
        }

        return new Result<>();
    }
}
