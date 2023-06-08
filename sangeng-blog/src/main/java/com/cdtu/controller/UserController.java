package com.cdtu.controller;

import com.cdtu.anotation.SystemLog;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.User;
import com.cdtu.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "用户", description = "用户相关接口")
public class UserController {
    @Resource
    UserService userService;
    @GetMapping("/userInfo")
    @SystemLog(businessName = "获取用户信息")
    @ApiOperation(value = "获取用户信息", notes = "获取用户详细信息")
    public ResponseResult<?> getUserInfo() {
        return userService.getUserInfo();
    }
    @PutMapping("/userInfo")
    @ApiOperation(value = "更新用户信息", notes = "更新用户详细信息")
    @ApiImplicitParam(name = "user", value = "用户对象")
    public ResponseResult<?> updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }
    @PostMapping("/register")
    @ApiOperation(value = "注册用户", notes = "注册用户账号")
    @ApiImplicitParam(name = "user", value = "用户对象")
    public ResponseResult<?> register(@RequestBody User user) {
        return userService.register(user);
    }
}
