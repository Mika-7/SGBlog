package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddUserDto;
import com.cdtu.domain.dto.ChangeStatusUserDto;
import com.cdtu.domain.dto.ListUserDto;
import com.cdtu.domain.dto.ModifyUserDto;
import com.cdtu.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Resource
    UserService userService;
    @GetMapping("/list")
    public ResponseResult<?> getUserList(
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam("pageSize")Integer pageSize,
            ListUserDto listUserDto) {
        return userService.getListUser(pageNum, pageSize, listUserDto);
    }
    @PostMapping
    public ResponseResult<?> addUser(@RequestBody AddUserDto addUserDto) {
        return userService.addUser(addUserDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult<?> removeUser(@PathVariable("ids") String ids) {
        String[] idArray = ids.split(",");
        for (String str_id : idArray) {
            Long id = Long.parseLong(str_id);
            userService.removeUser(id);
        }
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult<?> getUserDetailById(@PathVariable("id")Long userId) {
        return userService.getUserDetailById(userId);
    }
    @PutMapping
    public ResponseResult<?> modifyUser(@RequestBody ModifyUserDto modifyUserDto) {
        return userService.modifyUser(modifyUserDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult<?> changeStatus(
            @RequestBody ChangeStatusUserDto changeStatusUserDto) {
        return userService.changeStatus(changeStatusUserDto);
    }
}
