package com.lin.company_sales_management_system.controller;

import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.client.ClientPostData;
import com.lin.company_sales_management_system.entity.Client;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.ClientService;
import com.lin.company_sales_management_system.utils.Validator;
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
@RequestMapping("/company_sales_management_system/client")
@Api(tags = "客户模块")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // 客户信息查询
    @GetMapping("/getClients")
    @ApiOperation(value = "获取客户信息(2)")
    public Result<List<Client>> getClients(@RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String email, @RequestParam(required = false) String clientName, HttpServletRequest req) {
        ClientPostData clientInfo = new ClientPostData(phoneNumber, email, clientName);
        // 权限校验
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        return new Result<>(clientService.getClients(clientInfo));
    }

    // 客户信息修改
    @PutMapping("/update/{id}")
    @ApiOperation(value = "修改客户信息(2)")
    public Result<?> updateClientById(@RequestBody ClientPostData clientInfo, @PathVariable("id") Long id, HttpServletRequest req) {

        // 鉴权 + 校验信息
        Result<?> result = commonPart(clientInfo, req);
        if (!Objects.isNull(result)) return result;

        // 执行更新操作
        Map<String, Object> res = clientService.updateClientById(id,clientInfo);

        if (!Objects.isNull(res)) {
            String msg = (String) res.get("err");
            Integer code = (Integer) res.get("code");
            return new Result<>(code, msg);
        }

        return Result.ok();
    }

    // 客户信息添加
    @PostMapping("/add")
    @ApiOperation(value = "添加客户信息(2)")
    public Result<?> addClient(@RequestBody ClientPostData clientInfo, HttpServletRequest req) {
        // 鉴权 + 校验信息
        Result<?> result = commonPart(clientInfo, req);
        if (!Objects.isNull(result)) return result;

        // 执行添加操作
        Map<String, Object> res = clientService.addClient(clientInfo);

        if (!Objects.isNull(res)) {
            String msg = (String) res.get("err");
            Integer code = (Integer) res.get("code");
            return new Result<>(code, msg);
        }

        return Result.ok();
    }

    // 通过客户ID获取客户信息
    @GetMapping("/getClientById/{cliId}")
    @ApiOperation(value = "通过客户ID获取客户信息")
    public Result<Client> getClientById(@PathVariable("cliId")Long cliId) {
        Client client = clientService.getById(cliId);
        return new Result<>(client);
    }

    // 抽取公共部分出来
    private Result<?> commonPart(ClientPostData clientInfo, HttpServletRequest req) {
        // 权限校验
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");

        // 校验信息
        if (!Validator.isValidEmail(clientInfo.getEmail()))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"邮件地址有误");
        if (!Validator.isValidPhoneNumber(clientInfo.getPhoneNumber()))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"电话号码格式有误");
        if (!Validator.isLengthOk(clientInfo.getClientName(),1, 20))
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"员工姓名长度有误");

        return null;
    }
}
