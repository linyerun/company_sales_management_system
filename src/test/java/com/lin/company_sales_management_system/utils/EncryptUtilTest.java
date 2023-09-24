package com.lin.company_sales_management_system.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class EncryptUtilTest {

    @Test
    public void testEncrypt() {
        String pwd = "cangguan";
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        String encode = b.encode(pwd);
        System.out.println(encode);
    }
}
