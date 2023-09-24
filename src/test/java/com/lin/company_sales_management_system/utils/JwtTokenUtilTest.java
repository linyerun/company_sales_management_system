package com.lin.company_sales_management_system.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class JwtTokenUtilTest {

    @Test
    public void testJwt() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 11);
        map.put("identity", 2);
        map.put("username", "admin");

        // 生成token
        String token = JwtTokenUtil.createToken(map);
        System.out.println(token);

        // 解析token
        String[] keys = {"username", "identity", "id"};
        Map<String, Object> m = JwtTokenUtil.parseToken(token, keys);
        assert m != null; //断言
        System.out.println((Long) m.get("id"));
        System.out.println(new Date().getTime());
    }
}
