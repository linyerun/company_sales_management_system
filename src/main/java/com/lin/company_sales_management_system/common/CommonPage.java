package com.lin.company_sales_management_system.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页数据封装
 */
@Data
public class CommonPage<T> {
    @ApiModelProperty("当前页码")
    private Long pageNum;

    @ApiModelProperty("每页数量")
    private Long pageSize;

    @ApiModelProperty("总页数")
    private Long totalPage;

    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("分页数据")
    private List<T> list; // 采用泛型，适用任何排序

    public static <T> CommonPage<T> restPage(Page<T> pageInfo){
        CommonPage<T> result = new CommonPage<>();

        result.setPageNum(pageInfo.getCurrent());
        result.setPageSize(pageInfo.getSize());
        result.setTotalPage(pageInfo.getPages());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getRecords());

        return result;
    }
}
