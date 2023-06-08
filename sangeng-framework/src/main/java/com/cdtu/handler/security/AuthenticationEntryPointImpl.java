package com.cdtu.handler.security;

import com.alibaba.fastjson.JSON;
import com.cdtu.domain.ResponseResult;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证入口点
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        ResponseResult result = null;
        // InsufficientAuthenticationException
        if (e instanceof InsufficientAuthenticationException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, AppHttpCodeEnum.NEED_LOGIN.getMsg());
        }
        else if(e instanceof BadCredentialsException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR, AppHttpCodeEnum.LOGIN_ERROR.getMsg());
        }
        else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "认证出现问题");
        }
        // 响应前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
