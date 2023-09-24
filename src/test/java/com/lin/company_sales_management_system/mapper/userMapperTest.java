package com.lin.company_sales_management_system.mapper;

import com.lin.company_sales_management_system.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class userMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserMapper() {
        User user = new User();
        user.setPhoneNumber("1111");
        int i = userMapper.insert(user);
        System.out.println(i);
        System.out.println(user);
    }

    @Test
    public void testUpdatedAt() {
        User user = new User();
        user.setId(20L);
        user.setUsername("666");

        int i = userMapper.updateById(user);
        System.out.println(i);
    }

    @Test
    public void testDeleteUser() {
        userMapper.deleteBatchIds(Arrays.asList(9,10,11,12));
    }
}
