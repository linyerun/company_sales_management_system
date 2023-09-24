package com.lin.company_sales_management_system.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PurchasingListMapperTest {

    @Autowired
    private PurchasingListMapper purchasingListMapper;

    @Test
    public void testGetContractId() {
        Long id = purchasingListMapper.selectIdByContractId(1L);
        System.out.println("id: " + id);
    }
}
