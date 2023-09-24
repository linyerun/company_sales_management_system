package com.lin.company_sales_management_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.purchase_order.PurchaseOrderInfo;
import com.lin.company_sales_management_system.dto.purchase_order_info.PurchaseOrderInfoData;
import com.lin.company_sales_management_system.entity.PurchaseOrder;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.PurchaseOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
@RequestMapping("/company_sales_management_system/purchaseOrder")
@Api(tags = "进货单模块")
public class PurchaseOrderController {

    private final PurchaseOrderService poService;

    public PurchaseOrderController(PurchaseOrderService poService) {
        this.poService = poService;
    }

    // 创建进货单
    @PostMapping("/post")
    @ApiOperation(value = "创建进货单(2)")
    public Result<Object> post(@RequestBody PurchaseOrderInfo info, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 判断参数
        if (Objects.isNull(info.getGoodsCount()) || Objects.isNull(info.getGoodsId()))
            return new Result<>(1001, "参数存在空值");

        // 执行常见进货操作
        PurchaseOrder order = new PurchaseOrder();
        order.setState((byte)0);
        order.setGoodsCount(info.getGoodsCount());
        order.setGoodsId(info.getGoodsId());
        boolean isOk = poService.save(order);

        // 不ok
        if (!isOk)
            return new Result<>(500, "系统繁忙，操作失败，请稍后再进行尝试！");

        return new Result<>();
    }

    // 仓库管理员待处理进货单
    @GetMapping("/get")
    @ApiOperation(value = "获取待处理进货单(12)")
    public Result<List<PurchaseOrder>> getIngPurchaseOrder(HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (user.getIdentity().equals((byte) 0))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 执行获取操作
        List<PurchaseOrder> purchaseOrderList = poService
                .getBaseMapper()
                .selectList(new LambdaQueryWrapper<PurchaseOrder>().eq(PurchaseOrder::getState, 0));

        return new Result<>(purchaseOrderList);
    }

    // 仓库管理员改变进货单状态
    @PutMapping("/update/{goodsId}/{goodsCount}")
    @ApiOperation(value = "仓库管理员提交进货单进货信息(1)")
    public Result<Object> updateState(@RequestBody PurchaseOrderInfoData purchaseOrderInfoData, @PathVariable("goodsId")Long goodsId, @PathVariable("goodsCount")Long goodsCount, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 1))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 执行更新操作
        try {
            poService.updateStateCount(purchaseOrderInfoData, goodsId, goodsCount);
        } catch (Exception e) {
            String[] s = e.getMessage().split(";");
            return new Result<>(Integer.parseInt(s[0]), s[1]);
        }

        return new Result<>();
    }
}
