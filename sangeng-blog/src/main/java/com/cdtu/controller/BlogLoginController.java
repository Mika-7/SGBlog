package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.User;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.service.BlogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "登录", description = "登录相关接口")
public class BlogLoginController {
    @Resource
    private BlogLoginService blogLoginService;
    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录接口")
    @ApiImplicitParam(name = "user", value = "用户对象")
    public ResponseResult<?> login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            // 提示必须传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }
    @PostMapping("/logout")
    @ApiOperation(value = "登出", notes = "登出接口")
    public ResponseResult<?> logout() {
        return blogLoginService.logout();
    }
}
