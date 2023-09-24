package com.lin.company_sales_management_system.controller;

import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.purchase_order_info.PurchaseOrderInfoData;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.PurchaseOrderInfoService;
import com.lin.company_sales_management_system.utils.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-12-03
 */
@RestController
@RequestMapping("/company_sales_management_system/purchaseOrderInfo")
@Api(tags = "进货单信息模块")
public class PurchaseOrderInfoController {

    private final PurchaseOrderInfoService purchaseOrderInfoService;

    public PurchaseOrderInfoController(PurchaseOrderInfoService purchaseOrderInfoService) {
        this.purchaseOrderInfoService = purchaseOrderInfoService;
    }

    @PostMapping("/post")
    @ApiOperation(value = "提交进货单信息(1)")
    public Result<?> postPurchaseOrderInfo(@RequestBody PurchaseOrderInfoData purchaseOrderInfoData, HttpServletRequest req) {
        // 判断操作人是否有权限进行此操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 1))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");
        // 参数校验
        if (Validator.isNull(purchaseOrderInfoData) ||
                Validator.isNull(purchaseOrderInfoData.getPurchaseOrderId())||
                Validator.isNull(purchaseOrderInfoData.getCommentInfo())||
                Validator.isNull(purchaseOrderInfoData.getUnitPrice())
        ){
            return new Result<>(1001, "参数存在空值");
        }
        // 执行提交操作
        Map<String, Object> res = purchaseOrderInfoService.postInfo(purchaseOrderInfoData);
        if (!Objects.isNull(res)) {
            Integer code = (Integer) res.get("code");
            String msg = (String) res.get("err");
            return new Result<>(code, msg);
        }
        return new Result<>();
    }
}
