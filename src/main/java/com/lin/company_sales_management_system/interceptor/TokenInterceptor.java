package com.lin.company_sales_management_system.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.company_sales_management_system.cache.MyCache;
import com.lin.company_sales_management_system.cache.TimeoutUser;
import com.lin.company_sales_management_system.common.ResultCode;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        String token = req.getHeader("token");

        if (Objects.isNull(token)) {
            setRespMsg(ResultCode.UNAUTHORIZED.getCode(), "token值为空", resp);
            return false;
        }

        // 是否存在这个token
        TimeoutUser timeoutUser = MyCache.get(token);
        if (Objects.isNull(timeoutUser)) {
            setRespMsg(ResultCode.UNAUTHORIZED.getCode(), "该值为无效token值", resp);
            return false;
        }

        // 判断token是否过期
        if (timeoutUser.getTimeout() < new Date().getTime()) {
            MyCache.delete(token);  // 过期了就移除这个(还是存在很大的问题，存在过期还未移除的情况)
            return false;
        }

        // 更新一下过期时间
        timeoutUser.setTimeout(new Date().getTime() + 30 * 60 * 1000);

        req.setAttribute("user", timeoutUser.getUser().clone());

        return true;
    }

    private void setRespMsg(int code, String msg, HttpServletResponse resp) {
        resp.setContentType("application/json;charset=UTF-8");
        Map<String, Object> resData = new HashMap<>();
        resData.put("code", code);
        resData.put("msg", msg);
        try {
            String s = new ObjectMapper().writeValueAsString(resData);
            resp.getWriter().println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
