package com.lin.company_sales_management_system.service;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    String uploadOssFile(MultipartFile file);
}
