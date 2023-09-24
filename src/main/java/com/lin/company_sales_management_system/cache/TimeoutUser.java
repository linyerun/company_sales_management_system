package com.lin.company_sales_management_system.cache;

import com.lin.company_sales_management_system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeoutUser {
    private User user;
    private long timeout;
}
