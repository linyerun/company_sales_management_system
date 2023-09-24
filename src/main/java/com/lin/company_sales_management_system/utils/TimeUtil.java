package com.lin.company_sales_management_system.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    // 必须包含时分秒
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime parse(String time) {
        time += " 00:00:00";
        return LocalDateTime.parse(time, fmt);
    }
}
