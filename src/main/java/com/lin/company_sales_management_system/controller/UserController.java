package com.lin.company_sales_management_system.controller;

import com.lin.company_sales_management_system.cache.MyCache;
import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.user.UserInfo;
import com.lin.company_sales_management_system.dto.user.UserMsg;
import com.lin.company_sales_management_system.dto.user.UsernamePwd;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.service.UserService;
import com.lin.company_sales_management_system.utils.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@RestController
@RequestMapping("/company_sales_management_system/user")
@Api(tags = "用户模块")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 销售人员注册
    @PostMapping("/register")
    @ApiOperation(value = "新增销售人员(2)")
    public Result<?> register(@RequestBody UserInfo userInfo, HttpServletRequest req) {

        Result<?> result = commonPart(userInfo, req);
        if (!Objects.isNull(result)) return result;

        // 进行注册处理
        Map<String, Object> res = userService.register(userInfo);

        if (!Objects.isNull(res)) {
            String msg = (String) res.get("err");
            Integer code = (Integer) res.get("code");
            return new Result<>(code, msg);
        }

        return Result.ok();
    }

    // 销售人员修改
    @PutMapping("/update")
    @ApiOperation(value = "修改销售人员信息(2)")
    public Result<?> updateSalesman(@RequestBody UserMsg userInfo, HttpServletRequest req) {
        // 权限校验 + 参数校验
        Result<?> result = verifyUpdate(userInfo, req);
        if (!Objects.isNull(result)) return result;

        User user = new User();
        user.setId(userInfo.getId());
        user.setIdentity((byte) 0);
        user.setUsername(userInfo.getUsername());
        user.setPassword(userInfo.getPassword());
        user.setPhoneNumber(userInfo.getPhoneNumber());
        user.setEmail(userInfo.getEmail());
        user.setEmployeeName(userInfo.getEmployeeName());

        if (Objects.isNull(user.getIdentity()) || user.getIdentity() != 0)
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "你没有权限对此员工进行修改操作!");

        // 执行更新操作
        Map<String, Object> res = userService.updateSalesman(user);
        if (!Objects.isNull(res)) {
            String msg = (String) res.get("err");
            Integer code = (Integer) res.get("code");
            return new Result<>(code, msg);
        }

        return Result.ok();
    }

    // 销售人员查询
    @GetMapping("/query")
    @ApiOperation(value = "查询销售员工(2)")
    public Result<?> querySalesman(@RequestParam(value = "name", required = false) String name, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "你没有权限进行此操作");

        // 查询
        List<User> users = userService.querySalesman(name);

        return new Result<>(users);
    }

    // 登录
    @PostMapping("/login")
    @ApiOperation(value = "登录(all)")
    public Result<?> login(@RequestBody UsernamePwd usernamePwd) {
        String username = usernamePwd.getUsername();
        String password = usernamePwd.getPassword();

        // 参数校验
        if (username.length() < 6) {
            return Result.error("所输账号有误");
        } else if (password.length() < 6) {
            return Result.error("所输密码有误");
        }

        // 获取 token
        String token = userService.login(username, password);

        // 返回信息
        return new Result<>("登录成功", new HashMap<String, String>() {{
            put("token", token);
            put("identity", MyCache.get(token).getUser().getIdentity().toString());
        }});
    }

    // 抽取公共判断代码
    private Result<?> commonPart(UserInfo userInfo, HttpServletRequest req) {

        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "你没有权限进行此操作");

        // 参数校验
        if (!Validator.isValidEmail(userInfo.getEmail()))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "邮件地址有误");
        if (!Validator.isValidPhoneNumber(userInfo.getPhoneNumber()))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "电话号码格式有误");
        if (!Validator.isLengthOk(userInfo.getUsername(), 6, 30))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "账号长度有误");
        if (!Validator.isLengthOk(userInfo.getPassword(), 6, 50))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "密码长度有误");
        if (!Validator.isLengthOk(userInfo.getEmployeeName(), 1, 20))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "员工姓名长度有误");

        return null;
    }

    // 判断更新操作的
    private Result<?> verifyUpdate(UserMsg userInfo, HttpServletRequest req) {
        // 判断操作人是否有权限进行这个操作
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals((byte) 2))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "你没有权限进行此操作");

        // 参数校验
        if (!isNull(userInfo.getEmail()) && !Validator.isValidEmail(userInfo.getEmail()))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "邮件地址有误");
        else if (isNull(userInfo.getEmail()))
            userInfo.setEmail(null);

        if (!isNull(userInfo.getPhoneNumber()) && !Validator.isValidPhoneNumber(userInfo.getPhoneNumber()))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "电话号码格式有误");
        else if (isNull(userInfo.getPhoneNumber()))
            userInfo.setPhoneNumber(null);

        if (!isNull(userInfo.getUsername()) && !Validator.isLengthOk(userInfo.getUsername(), 6, 30))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "账号长度有误");
        else if (isNull(userInfo.getUsername()))
            userInfo.setUsername(null);

        if (!isNull(userInfo.getPassword()) && !Validator.isLengthOk(userInfo.getPassword(), 6, 50))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "密码长度有误");
        else if (isNull(userInfo.getPassword()))
            userInfo.setPassword(null);

        if (!isNull(userInfo.getEmployeeName()) && !Validator.isLengthOk(userInfo.getEmployeeName(), 1, 20))
            return new Result<>(ResultCode.FORBIDDEN.getCode(), "员工姓名长度有误");
        else if (isNull(userInfo.getEmployeeName()))
            userInfo.setEmployeeName(null);

        return null;
    }

    // 通过销售员工ID获取员工
    @GetMapping("/getEmployeeById/{empId}")
    @ApiOperation(value = "通过销售员工ID获取员工")
    public Result<User> getEmployeeById(@PathVariable("empId") Long empId) {
        User emp = userService.getById(empId);
        emp.setPassword(null);
        return new Result<>(emp);
    }

    private boolean isNull(String s) {
        return s == null || s.length() == 0;
    }
}
