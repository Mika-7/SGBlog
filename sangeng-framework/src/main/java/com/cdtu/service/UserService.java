package com.cdtu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddUserDto;
import com.cdtu.domain.dto.ChangeStatusUserDto;
import com.cdtu.domain.dto.ListUserDto;
import com.cdtu.domain.dto.ModifyUserDto;
import com.cdtu.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author mika
 * @since 2023-05-13 09:59:15
 */
public interface UserService extends IService<User> {

    ResponseResult<?> getUserInfo();

    ResponseResult<?> updateUserInfo(User user);

    ResponseResult<?> register(User user);

    ResponseResult<?> getListUser(Integer pageNum, Integer pageSize, ListUserDto listUserDto);

    ResponseResult<?> addUser(AddUserDto addUserDto);

    ResponseResult<?> removeUser(Long userId);

    ResponseResult<?> getUserDetailById(Long userId);

    ResponseResult<?> modifyUser(ModifyUserDto modifyUserDto);

    ResponseResult<?> changeStatus(ChangeStatusUserDto changeStatusUserDto);
}
