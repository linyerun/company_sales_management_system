package com.lin.company_sales_management_system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.contract.ClientInfo;
import com.lin.company_sales_management_system.dto.contract.ContractInfo;
import com.lin.company_sales_management_system.dto.contract.Img;
import com.lin.company_sales_management_system.entity.Contract;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.ContractService;
import com.lin.company_sales_management_system.utils.TimeUtil;
import com.lin.company_sales_management_system.utils.Validator;
import com.lin.company_sales_management_system.vo.contract.State;
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
@RequestMapping("/company_sales_management_system/contract")
@Api(tags = "合同模块")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    // 新增合同
    @PostMapping("/record")
    @ApiOperation(value = "合同录入(2)")
    public Result<?> record(@RequestBody ContractInfo contractInfo, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // TODO 校验contractInfo属性

        try {
            Map<String, Object> res = contractService.record(contractInfo);
            if (!Objects.isNull(res)) {
                String msg = (String) res.get("err");
                Integer code = (Integer) res.get("code");
                return new Result<>(code, msg);
            }
        } catch (Exception e) {
            String[] s = e.getMessage().split(";");
            return new Result<>(Integer.parseInt(s[0]), s[1]);
        }

        return Result.ok();
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "根据合同ID获取合同信息(012)")
    public Result<Contract> getContractById(@PathVariable("id")Long id) {
        if (Objects.isNull(id)) return new Result<>(1001, "参数不能为空值");

        Contract contract = contractService.getById(id);

        return new Result<>(contract);
    }

    // 统计一段时间内公司的销售总额
    @GetMapping("/getSumByTime")
    @ApiOperation(value = "计一段时间内公司的销售总额(2)")
    public Result<Sum> getSumByTime(@RequestParam("startTime")String startTime, @RequestParam("endTime")String endTime, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");
        // 校验参数
        if (Objects.isNull(startTime) || Objects.isNull(endTime))
            return new Result<>(1001, "参数存在空值");

        BigDecimal sum = contractService.getSumByTime(TimeUtil.parse(startTime), TimeUtil.parse(endTime));

        return new Result<>(new Sum(sum));
    }

    // 针对不同客户的销售额
    @GetMapping("/getSumByClientId/{clientId}")
    @ApiOperation(value = "针对不同客户的销售额(2)")
    public Result<Sum> getSumByClientId(@PathVariable("clientId")Long clientId, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");
        // 校验参数
        if (Objects.isNull(clientId))
            return new Result<>(1001, "参数不能为空值");

        BigDecimal sum = contractService.getSumByClientId(clientId);

        return new Result<>(new Sum(sum));
    }

    // 销售人员查看合同执行状态
    @GetMapping("/getState")
    @ApiOperation(value = "销售人员查看合同执行状态(02)")
    public Result<List<State>> getContractStateByEmployeeId(HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (user.getIdentity().equals((byte) 1))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");
        Long employeeId = user.getId();
        // 校验参数
        if (Objects.isNull(employeeId))
            return new Result<>(1001, "参数不能为空值");

        List<State> states = contractService.getContractStatesByEmployeeId(employeeId);

        return new Result<>(states);
    }

    // 查询一段时间内自己的销售业绩
    @GetMapping("/empSum/get")
    @ApiOperation(value = "销售员工查询一段时间内自己的销售业绩(02)")
    public Result<Sum> getEmployeeSumByTime(@RequestParam("startTime") String startTime, @RequestParam("endTime")String endTime, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (user.getIdentity().equals((byte) 1))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 从user中获取值
        Long employeeId = user.getId();

        // 校验参数
        if (Objects.isNull(startTime) || Objects.isNull(endTime))
            return new Result<>(1001, "参数存在空值");

        // 查询
        BigDecimal sum = contractService.getEmployeeSumByTime(employeeId, TimeUtil.parse(startTime), TimeUtil.parse(endTime));

        return new Result<>(new Sum(sum));
    }

    // 根据合同ID获取快递地址和客户邮件
    @GetMapping("/clientInfo/get/{contractId}")
    @ApiOperation(value = "根据合同ID获取发货信息(12)")
    public Result<ClientInfo> getClientInfo(@PathVariable("contractId")Long contractId, HttpServletRequest req) {
        // 参数校验
        if (Objects.isNull(contractId))
            return new Result<>(1001, "参数为空");

        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (user.getIdentity().equals((byte) 0))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 执行查询
        ClientInfo clientInfo = contractService.getClientInfo(contractId);

        return new Result<>(clientInfo);
    }

    // 获取所有合同
    @GetMapping("/getAllContract")
    @ApiOperation(value = "获取所有合同信息(2)")
    public Result<List<Contract>> getAll(HttpServletRequest req) {
        // 判断是否有权限
        Result<?> result = Validator.verifyIdentity(req, (byte) 2);
        if (!Objects.isNull(result)) {
            return new Result<>(result.getCode(), result.getMsg());
        }
        // 获取数据
        List<Contract> contracts = contractService.getBaseMapper().selectList(null);
        return new Result<>(contracts);
    }

    // 修改合同客户
    @PutMapping("/update/client/{cliId}/{contractId}")
    @ApiOperation(value = "修改合同客户(2)")
    public Result<?> updateContractCliIdById(@PathVariable("cliId") Long cliId, @PathVariable("contractId") Long contractId, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 参数有问题进行返回
        if (Objects.isNull(cliId) || Objects.isNull(contractId)) {
            return Result.error(ResultCode.PARAM_NOT_VALID);
        }
        boolean update = contractService
                .update(Wrappers
                        .<Contract>lambdaUpdate()
                        .set(Contract::getClientId, cliId)
                        .eq(Contract::getId, contractId)
                );
        if (update) {
            return new Result<>();
        }
        return Result.error();
    }

    // 修改合同员工
    @PutMapping("/update/employee/{empId}/{contractId}")
    @ApiOperation(value = "修改合同员工(2)")
    public Result<?> updateContractEmpIdById(@PathVariable("empId") Long empId, @PathVariable("contractId") Long contractId, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 参数有问题进行返回
        if (Objects.isNull(empId) || Objects.isNull(contractId)) {
            return Result.error(ResultCode.PARAM_NOT_VALID);
        }
        boolean update = contractService
                .update(Wrappers
                        .<Contract>lambdaUpdate()
                        .set(Contract::getUserId, empId)
                        .eq(Contract::getId, contractId)
                );
        if (update) {
            return new Result<>();
        }
        return Result.error();
    }

    // 修改合同图片
    @PutMapping("/update/pic/{contractId}")
    @ApiOperation(value = "修改合同图片(2)")
    public Result<?> updateContractPicById(@PathVariable("contractId")Long contractId, @RequestBody Img img, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 参数有问题进行返回
        if (Objects.isNull(img) || Objects.isNull(contractId) || Objects.isNull(img.getUrl()) || img.getUrl().length() == 0) {
            return Result.error(ResultCode.PARAM_NOT_VALID);
        }
        boolean update = contractService
                .update(Wrappers
                        .<Contract>lambdaUpdate()
                        .set(Contract::getContractPic, img.getUrl())
                        .eq(Contract::getId, contractId)
                );
        if (update) {
            return new Result<>();
        }
        return Result.error();
    }
}
