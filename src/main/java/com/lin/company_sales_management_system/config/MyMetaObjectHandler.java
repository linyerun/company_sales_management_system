package com.lin.company_sales_management_system.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("执行插入操作，进行时间的填充");
        this.setFieldValByName("createdAt", LocalDateTime.now(),metaObject);
        // 这里加了，外面注解还是要加上 INSERT_UPDATE才行，不然不会填充updateAt字段
        this.setFieldValByName("updatedAt", LocalDateTime.now(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("执行更新操作，进行时间的填充");
        this.setFieldValByName("updatedAt", LocalDateTime.now(),metaObject);
    }
}
