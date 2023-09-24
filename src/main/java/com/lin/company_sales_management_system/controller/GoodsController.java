package com.lin.company_sales_management_system.controller;

import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.goods.GoodInfo;
import com.lin.company_sales_management_system.entity.Goods;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.GoodsService;
import com.lin.company_sales_management_system.utils.Validator;
import com.lin.company_sales_management_system.vo.contract.Sum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
@RequestMapping("/company_sales_management_system/goods")
@Api(tags = "仓库货物模块")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // 返回所有商品
    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有商品(all)")
    public Result<List<Goods>> getAll() {
        List<Goods> goods = goodsService.getBaseMapper().selectList(null);
        goods.forEach(g -> {
            g.setCreatedAt(null);
            g.setUpdatedAt(null);
        });
        return new Result<>(goods);
    }

    // 不同商品的销售额
    @GetMapping("/getSum/{goodsId}")
    @ApiOperation(value = "根据货物ID获取货物销售额(2)")
    public Result<Sum> getSumByGoodsId(@PathVariable("goodsId")Long goodsId, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");
        // 校验参数
        if (Objects.isNull(goodsId)) return new Result<>(1001, "参数不能为空值");

        BigDecimal sum = goodsService.getSumByGoodsId(goodsId);

        if (Objects.isNull(sum)) return new Result<>(1001, "该货物不存在!");

        return new Result<>(new Sum(sum));
    }

    // 新增货物
    @PostMapping("/post")
    @ApiOperation(value = "新增货物(2)")
    public Result<?> post(@RequestBody GoodInfo goodInfo, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 参数校验
        if (isNull(goodInfo.getGoodsCount()) || isNull(goodInfo.getGoodsName()) || isNull(goodInfo.getUnitPrice()))
            return new Result<>(1001, "参数存在空值");

        // 执行新增操作
        Map<String, Object> res = goodsService.post(goodInfo);

        if (!isNull(res)) {
            return new Result<>((Integer) res.get("code"), (String) res.get("err"));
        }

        return new Result<>();
    }


    // 根据货物ID获取货物信息
    @GetMapping("/get/{goodId}")
    @ApiOperation(value = "根据货物ID获取货物信息")
    public Result<Goods> getById(@PathVariable("goodId")Long goodId) {
        if (Validator.isNull(goodId)) return new Result<>(1001, "参数为空值");
        // 获取货物
        Goods goods = goodsService.getById(goodId);

        return new Result<>(goods);
    }

    private boolean isNull(Object o) {
        if (o instanceof String) {
            return ((String) o).length() == 0;
        }
        return Objects.isNull(o);
    }
}
