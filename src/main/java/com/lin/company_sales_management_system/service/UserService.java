package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.user.UserInfo;
import com.lin.company_sales_management_system.entity.User;

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
public interface UserService extends IService<User> {
    String login(String username, String password);
    Map<String, Object> register(UserInfo userInfo);
    Map<String, Object> updateSalesman(User user);
    List<User> querySalesman(String name);
}
