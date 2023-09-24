package com.lin.company_sales_management_system.solve_bug;

import com.lin.company_sales_management_system.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ByteEqualsTest {

    @Test
    public void bytesTest() {
        User user = new User();
        user.setIdentity((byte) 1);
        System.out.println(user.getIdentity() == (byte) 2);
        System.out.println(user.getIdentity().equals((byte) 2));
        System.out.println(!user.getIdentity().equals((byte) 2));
    }
}
