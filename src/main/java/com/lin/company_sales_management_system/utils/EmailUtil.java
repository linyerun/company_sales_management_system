package com.lin.company_sales_management_system.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 邮件发送工具类
 */
@Component
public class EmailUtil {

    private final JavaMailSender mailSender;

    private static final String senderEmail = "2338244917@qq.com";

    // 自动注入
    public EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String title,String content,String... emails) {
        //SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //设置标题
        mailMessage.setSubject(title);
        //设置内容
        mailMessage.setText(content);
        //收件人(可以是多个)
        mailMessage.setTo(emails);
        //发送人
        mailMessage.setFrom(senderEmail);
        //发送
        mailSender.send(mailMessage);
    }
}
