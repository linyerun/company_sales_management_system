package com.lin.company_sales_management_system.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用返回结果
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "通用返回对象")
@Data
public class Result<T> {

    @ApiModelProperty(value = "返回码", position = 1)
    private int code;
    @ApiModelProperty(value = "提示信息", position = 2)
    private String msg;
    @ApiModelProperty(value = "数据", position = 3)
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
        this(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public Result(int code, String msg) {
        this(code, msg, null);
    }

    /**
     * 有返回值的需要使用构造方法构造，因为使用了泛型
     */
    public Result(String msg, T data) {
        this(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public Result(T data) {
        this(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功，没有返回值的
     */
    public static Result<?> ok() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 自定义返回信息的成功返回
     *
     * @param msg 信息
     */
    public static Result<?> ok(String msg) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg);
    }

    /**
     * 无返回值的失败
     */
    public static Result<?> error() {
        return new Result<>(ResultCode.INTERNAL_SERVER_ERROR.getCode(), ResultCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    /**
     * 自定义信息的失败返回
     *
     * @param msg 信息
     */
    public static Result<?> error(String msg) {
        return new Result<>(ResultCode.PARAM_NOT_VALID.getCode(), msg);
    }

    /**
     * 其他错误情况
     *
     */
    public static Result<?> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage());
    }
}
