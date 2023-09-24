package com.lin.company_sales_management_system.utils;

import com.lin.company_sales_management_system.common.Result;
import com.lin.company_sales_management_system.common.ResultCode;
import com.lin.company_sales_management_system.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.regex.Pattern;

public class Validator {

    public static boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }

    public static boolean isValidPhoneNumber(String phoneNumber){
        if(Objects.isNull(phoneNumber) || phoneNumber.length() != 11){
            return false;
        }
        return Pattern.matches("^1[3-9]\\d{9}$", phoneNumber);
    }

    public static boolean isLengthOk(String param, int minLen, int maxLen) {
        if (param == null) return false;
        int len = param.length();
        return len >= minLen && len <= maxLen;
    }

    public static boolean isNull(Object o) {
        if (o instanceof String) {
            return ((String) o).length() == 0;
        }
        return Objects.isNull(o);
    }

    public static Result<?> verifyIdentity(HttpServletRequest req, byte identity) {
        User user = (User) req.getAttribute("user");
        if (!user.getIdentity().equals(identity)){
            return new Result<>(ResultCode.FORBIDDEN.getCode(),"你没有权限进行此操作");
        }
        return null;
    }
}
