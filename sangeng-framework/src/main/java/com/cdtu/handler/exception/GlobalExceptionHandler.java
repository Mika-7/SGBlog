package com.cdtu.handler.exception;

import com.cdtu.domain.ResponseResult;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
// @ControllerAdvice 和 @ResponseBody 等价于 一个注解 @RestControllerAdvice
/**
 * 全局异常处理器
 */
public class GlobalExceptionHandler {
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e) {
        // 打印异常信息
        log.error("出现系统异常", e);
        // 从异常中提取信息，封装返回响应
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        // 打印异常信息
        log.error("出现系统异常", e);
        // 从异常中提取信息，封装返回响应
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, e.getMessage());
    }
}
