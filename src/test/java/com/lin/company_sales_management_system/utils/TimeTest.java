package com.lin.company_sales_management_system.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class TimeTest {

    @Test
    public void localTimeParseTest() {
        LocalDateTime time = TimeUtil.parse("2022-12-20");
        System.out.println(time);
    }
}
