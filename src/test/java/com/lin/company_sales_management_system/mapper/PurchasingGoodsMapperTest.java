package com.lin.company_sales_management_system.mapper;

import com.lin.company_sales_management_system.entity.PurchasingGoods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PurchasingGoodsMapperTest {

    @Autowired
    private PurchasingGoodsMapper pgMapper;

    @Test
    public void insertBatchTest() {
        List<PurchasingGoods> list = new ArrayList<>();
        PurchasingGoods p1 = new PurchasingGoods();
        p1.setState((byte) 0);
        p1.setPurchasingCount(10L);
        p1.setGoodsId(1L);
        p1.setPurchasingListId(666L);
        p1.setCreatedAt(LocalDateTime.now());
        p1.setUpdatedAt(p1.getCreatedAt());
        list.add(p1);
        list.add(p1);
        list.add(p1);
        list.add(p1);
        list.add(p1);
        long pre = System.currentTimeMillis();
        int affectRows = pgMapper.insertBatch(list);
        System.out.println(System.currentTimeMillis()-pre);
        System.out.println(affectRows);
    }
}
