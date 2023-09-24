package com.lin.company_sales_management_system.exception_handler;

import com.lin.company_sales_management_system.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理对象
 * 直接捕获异常，并返回给前端信息
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 统一处理异常的方法，所有异常统一返回500错误码
     * @param e 异常
     * @return 返回值必须是ResponseEntity，不然会报404错误
     */
    @ExceptionHandler
    public ResponseEntity<Result> service(Exception e){
        //输出异常信息
        e.printStackTrace();
        return new ResponseEntity<>(Result.error(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
