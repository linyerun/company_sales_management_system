package com.lin.company_sales_management_system.controller;

import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.purchasing_goods.PurchasingGoodsInfo;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.PurchasingGoodsService;
import com.lin.company_sales_management_system.service.PurchasingListService;
import com.lin.company_sales_management_system.vo.purchasing_goods.PurchasingGoodsData;
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
@RequestMapping("/company_sales_management_system/purchasingGoods")
@Api(tags = "采购商品模块")
public class PurchasingGoodsController {

    private final PurchasingGoodsService purchasingGoodsService;
    private final PurchasingListService purchasingListService;

    public PurchasingGoodsController(PurchasingGoodsService purchasingGoodsService, PurchasingListService purchasingListService) {
        this.purchasingGoodsService = purchasingGoodsService;
        this.purchasingListService = purchasingListService;
    }

    // 根据采购清单获取采购商品信息(不拦截这个接口)
    @GetMapping("/get/{contractId}")
    @ApiOperation(value = "根据合同ID获取采购商品信息(all)")
    public Result<List<PurchasingGoodsData>> getPurchasingGoodsByPurchasingListId(@PathVariable("contractId")Long contractId) {
        if (Objects.isNull(contractId))
            return new Result<>(1001, "采购清单ID为空");

        // 获取采购清单ID
        Long purchasingListId = purchasingListService.selectIdByContractId(contractId);

        List<PurchasingGoodsData> res = purchasingGoodsService.getPurchasingGoodsByPurchasingListId(purchasingListId);

        return new Result<>(res);
    }

    @GetMapping("/getIng/{purchasingListId}")
    @ApiOperation(value = "根据采购清单ID获取未发货商品信息(2)")
    public Result<List<PurchasingGoodsData>> getIngByPurchasingListId(@PathVariable("purchasingListId")Long purchasingListId, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        if (Objects.isNull(purchasingListId))
            return new Result<>(1001, "采购清单ID为空");

        // 在上面的接口基础上加个判断即可，毕竟那个接口给客户看的，商品状态肯定是未发货状态
        List<PurchasingGoodsData> res = purchasingGoodsService.getPurchasingGoodsByPurchasingListId(purchasingListId);

        return new Result<>(res);
    }

    // 合同新增采购商品
    @PostMapping("/post/{contractId}")
    @ApiOperation(value = "合同新增采购商品")
    public Result<?> addPurchasingGoods(@RequestBody PurchasingGoodsInfo purchasingGoodsInfo, @PathVariable("contractId") Long contractId, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        try {
            purchasingGoodsService.addPurchasingGoods(purchasingGoodsInfo, contractId);
        } catch (Exception e) {
            String[] s = e.getMessage().split(";");
            Result<?> res = new Result<>();
            res.setCode(Integer.parseInt(s[0]));
            res.setMsg(s[1]);
            return res;
        }
        return Result.ok();
    }

    // 合同删除采购商品
    @DeleteMapping("/delete/{contractId}/{pgId}")
    @ApiOperation(value = "移除采购清单商品")
    public Result<?> deleteByIds(@PathVariable("contractId") Long contractId, @PathVariable("pgId") Long pgId, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        if (Objects.isNull(contractId) || Objects.isNull(pgId)) {
            return Result.error(ResultCode.PARAM_NOT_VALID);
        }
        try {
            purchasingGoodsService.deleteByIds(contractId, pgId);
        } catch (Exception e) {
            String[] s = e.getMessage().split(";");
            Result<?> res = new Result<>();
            res.setCode(Integer.parseInt(s[0]));
            res.setMsg(s[1]);
            return res;
        }
        return Result.ok();
    }
}
