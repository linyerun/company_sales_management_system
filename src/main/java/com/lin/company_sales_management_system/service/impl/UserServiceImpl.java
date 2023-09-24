package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.cache.MyCache;
import com.lin.company_sales_management_system.cache.TimeoutUser;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.dto.user.UserInfo;
import com.lin.company_sales_management_system.entity.User;
import com.lin.company_sales_management_system.mapper.UserMapper;
import com.lin.company_sales_management_system.service.UserService;
import com.lin.company_sales_management_system.utils.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder bcpEncoder;

    public UserServiceImpl(BCryptPasswordEncoder bcpEncoder) {
        this.bcpEncoder = bcpEncoder;
    }

    @Override
    public String login(String username, String password) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 账号不存在
        if (Objects.isNull(user)) return null;
        // 校验密码
        boolean res = bcpEncoder.matches(password, user.getPassword());
        // 密码错误
        if (!res) return null;

        // 封装需要生成信息的map
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("id", user.getId());
        map.put("phoneNumber", user.getPhoneNumber());
        map.put("email", user.getEmail());
        map.put("identity", user.getIdentity());
        map.put("employeeName", user.getEmployeeName());

        // 生成并返回token
        String token = JwtTokenUtil.createToken(map);

        // 将信息放到缓存里面
        MyCache.put(token, new TimeoutUser(user,new Date().getTime() + 30 * 60 * 1000));

        return token;
    }

    @Override
    public Map<String, Object> register(UserInfo userInfo) {
        Map<String, Object> res = new HashMap<>();
        // 查看username是否重复
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userInfo.getUsername()));
        if (!Objects.isNull(user)) {
            res.put("err", "账号重复，不可用!");
            res.put("code", ResultCode.PARAM_NOT_VALID.getCode());
            return res;
        }

        user = new User();
        user.setUsername(userInfo.getUsername());
        user.setPassword(bcpEncoder.encode(userInfo.getPassword())); // 加密
        user.setEmployeeName(userInfo.getEmployeeName());
        user.setEmail(userInfo.getEmail());
        user.setPhoneNumber(userInfo.getPhoneNumber());
        user.setIdentity((byte)0);

        int returnValue = baseMapper.insert(user);

        if (returnValue != 1) {
            res.put("err", "系统繁忙，请稍后再尝试");
            res.put("code", 500);
            return res;
        }

        return null;
    }

    @Override
    public Map<String, Object> updateSalesman(User user) {
        // 修改了密码就对密码进行加密
        if (!Objects.isNull(user.getPassword())) {
            user.setPassword(bcpEncoder.encode(user.getPassword()));
        }
        int returnValue = baseMapper.updateById(user);
        if (returnValue != 1) {
            return new HashMap<String, Object>(){{
               put("code", 500);
               put("err", "系统繁忙,修改失败,请稍后再进行尝试!");
            }};
        }
        return null;
    }

    @Override
    public List<User> querySalesman(String name) {
        boolean flag = !Objects.isNull(name) && name.length() > 0;
        List<User> users = baseMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .like(flag, User::getEmployeeName, name)
                        .eq(User::getIdentity, 0)
        );
        users.forEach(user -> user.setPassword(null));
        return users;
    }
}
