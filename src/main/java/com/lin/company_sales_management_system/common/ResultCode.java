package com.lin.company_sales_management_system.common;

/**
 * 常用API返回对象
 */
public enum ResultCode implements IErrorCode {

    // 返回对象枚举类型
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "不能满足请求"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方式不允许"),
    INTERNAL_SERVER_ERROR(500, "服务器错误"),
    UNAUTHORIZED(601, "token过期或token值为空"),
    PARAM_NOT_VALID(1001, "参数无效");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
