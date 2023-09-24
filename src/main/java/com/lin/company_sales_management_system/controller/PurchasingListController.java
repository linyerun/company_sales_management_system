package com.lin.company_sales_management_system.controller;

import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.purchasing_list.AddrInfo;
import com.lin.company_sales_management_system.entity.PurchasingList;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.PurchasingListService;
import com.lin.company_sales_management_system.utils.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@RestController
@RequestMapping("/company_sales_management_system/purchasingList")
@Api(tags = "采购清单模块")
public class PurchasingListController {

    private final PurchasingListService purchasingListService;

    public PurchasingListController(PurchasingListService purchasingListService) {
        this.purchasingListService = purchasingListService;
    }

    // 返回不同状态的清单列表(销售管理员才能使用的权限)
    @GetMapping("/getIng")
    @ApiOperation(value = "返回待处理清单列表(2)")
    public Result<List<PurchasingList>> getPurchasingListIng(HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        List<PurchasingList> list = purchasingListService.getPurchasingListByState(PurchasingListService.ING);
        return new Result<>(list);
    }

    @GetMapping("/getEnd")
    @ApiOperation(value = "返回已处理完成清单列表(2)")
    public Result<List<PurchasingList>> getPurchasingListEnd(HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        List<PurchasingList> list = purchasingListService.getPurchasingListByState(PurchasingListService.End);
        return new Result<>(list);
    }

    // 根据合同ID填写采购清单地址
    @PostMapping("/postAddr")
    @ApiOperation(value = "根据合同ID填写采购清单地址(cli)")
    public Result<?> postAddress(@RequestBody AddrInfo addrInfo) {
        // 信息校验
        if (Validator.isNull(addrInfo.getAddress()) || Validator.isNull(addrInfo.getContractId()))
            return new Result<>(1001, "参数存在空值");

        // TODO 应该要再加一个地址为空判断避免客户重复提交地址的

        // 进行业务处理
        try {
            purchasingListService.postAddress(addrInfo);
        } catch (Exception e) {
            String[] s = e.getMessage().split(";");
            return new Result<>(Integer.parseInt(s[0]), s[1]);
        }

        return new Result<>();
    }
}
