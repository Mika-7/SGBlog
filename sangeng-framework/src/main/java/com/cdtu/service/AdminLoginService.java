package com.cdtu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.User;

public interface AdminLoginService extends IService<User> {
    ResponseResult<?> login(User user);

    ResponseResult<?> logout();
}
