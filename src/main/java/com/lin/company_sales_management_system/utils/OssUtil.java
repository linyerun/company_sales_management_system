package com.lin.company_sales_management_system.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class OssUtil {
    public static OSS getOssClient() {
        return new OSSClientBuilder().build(
                ConstantPropertiesUtil.END_POINT,
                ConstantPropertiesUtil.KEY_ID,
                ConstantPropertiesUtil.KEY_SECRET
        );
    }
}
