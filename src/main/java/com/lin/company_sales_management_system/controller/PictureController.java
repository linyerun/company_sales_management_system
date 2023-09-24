package com.lin.company_sales_management_system.controller;

import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.service.PictureService;
import com.lin.company_sales_management_system.vo.picture.ReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/pic")
@Api(tags = "OSS")
public class PictureController {

    private final PictureService service;

    public PictureController(PictureService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public Result<ReturnData> uploadOssFile(MultipartFile file) {
        String url = service.uploadOssFile(file);
        if (Objects.isNull(url)) return new Result<>(500,"上传失败，请稍后再次尝试。",null);
        return new Result<>(500,"成功",new ReturnData(url));
    }
}
