package com.lin.company_sales_management_system.service.impl;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSS;
import com.lin.company_sales_management_system.service.PictureService;
import com.lin.company_sales_management_system.utils.ConstantPropertiesUtil;
import com.lin.company_sales_management_system.utils.OssUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    @Override
    public String uploadOssFile(MultipartFile file) {
        OSS ossClient = OssUtil.getOssClient();
        //这两个用于上传图片和拼接返回的url
        String endpoint = ConstantPropertiesUtil.END_POINT;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        try {
            //获取前端传过来的文件
            InputStream inputStream = file.getInputStream();

            //构建文件名和文件夹
            String filename = file.getOriginalFilename();//获取文件名
            String uuid = UUID
                    .randomUUID()
                    .toString()
                    .replaceAll("-", "");

            filename = uuid + filename;

            String dataPath = new DateTime().toString("yyyy-MM-dd");

            filename = "javaEE-homework-system/" + dataPath + "/" + filename;

            //上传文件
            ossClient.putObject(bucketName, filename, inputStream);

            //构建返回的url
            return "https://" + bucketName + "." + endpoint + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            //关闭连接
            ossClient.shutdown();
        }
    }
}
