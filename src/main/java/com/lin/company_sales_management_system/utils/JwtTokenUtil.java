package com.lin.company_sales_management_system.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {
    // 加盐密钥
    private static final String secret = "jwt-lin-ye-run-4234-salesSystem";

    // 创建token
    public static String createToken(Map<String, Object> map) {

        // 自定义信息
        JwtBuilder claim = Jwts.builder()
                // 设置签名算法和加盐秘钥
                .signWith(SignatureAlgorithm.HS256, secret)
                // 自定义内容接受一个map
                .setClaims(map);

        // 返回生成的token
        return claim.compact();
    }

    // 解析token
    public static Map<String, Object> parseToken(String token, String[] keys) {
        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        // 检验token是否过期
        long expire = body.get("expire", Long.class);
        long now = new Date().getTime();
        // 说明token过期了，就不返回信息了
        if (now > expire) return null;

        Map<String, Object> map = new HashMap<>();
        for (String key : keys) {
            map.put(key,  body.get(key));
        }
        return map;
    }
}
