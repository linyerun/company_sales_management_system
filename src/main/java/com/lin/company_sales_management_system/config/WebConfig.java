package com.lin.company_sales_management_system.config;

import com.lin.company_sales_management_system.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new TokenInterceptor())
                // 只有我们自己写的接口会被进行拦截，避免把swagger也给拦截掉了
                .addPathPatterns("/company_sales_management_system/**")
                .excludePathPatterns("/company_sales_management_system/user/login")
                .excludePathPatterns("/company_sales_management_system/purchasingGoods/get/**")
                // 让客户也可以获取合同信息
                .excludePathPatterns("/company_sales_management_system/contract/get/**")
                // 让客户可以提交收货地址
                .excludePathPatterns("/company_sales_management_system/purchasingList/postAddr");
    }

    // 配置给swagger放行
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("doc.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
}
