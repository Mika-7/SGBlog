package com.cdtu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddRoleDto;
import com.cdtu.domain.dto.ChangeStatusRoleDto;
import com.cdtu.domain.dto.ListRoleDto;
import com.cdtu.domain.entity.Role;
import com.cdtu.domain.dto.ModifyRoleDto;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author mika
 * @since 2023-05-18 17:00:04
 */
public interface RoleService extends IService<Role> {

    List<String> getRoleKeyByUserId(Long userId);

    ResponseResult<?> listRole(Integer pageNum, Integer pageSize, ListRoleDto listRoleDto);

    ResponseResult<?> changeStatus(ChangeStatusRoleDto changeStatusRoleDto);

    ResponseResult<?> addRole(AddRoleDto addRoleDto);

    ResponseResult<?> removeRole(Long roleId);

    ResponseResult<?> getRole(Long roleId);

    ResponseResult<?> modifyRole(ModifyRoleDto modifyRoleDto);
    ResponseResult<?> getAllRole();

}
