package com.cdtu.exception;

import com.cdtu.enums.AppHttpCodeEnum;

/**
 * 系统异常类：
 */
public class SystemException extends RuntimeException{
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    public SystemException(AppHttpCodeEnum enums) {
        this.code = enums.getCode();
        this.msg = enums.getMsg();
    }
}
