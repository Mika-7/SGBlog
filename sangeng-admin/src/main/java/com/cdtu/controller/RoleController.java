package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddRoleDto;
import com.cdtu.domain.dto.ChangeStatusRoleDto;
import com.cdtu.domain.dto.ListRoleDto;
import com.cdtu.domain.dto.ModifyRoleDto;
import com.cdtu.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Resource
    RoleService roleService;
    @GetMapping("/list")
    public ResponseResult<?> listRole(
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam("pageSize")Integer pageSize,
            ListRoleDto listRoleDto) {
        return roleService.listRole(pageNum, pageSize, listRoleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult<?> getRole(@PathVariable("id")Long id) {
        return roleService.getRole(id);
    }

    @PutMapping("/changeStatus")
    public ResponseResult<?> changeStatus(@RequestBody ChangeStatusRoleDto changeStatusRoleDto) {
        return roleService.changeStatus(changeStatusRoleDto);
    }
    @PutMapping
    public ResponseResult<?> modifyRole(@RequestBody ModifyRoleDto modifyRoleDto) {
        return roleService.modifyRole(modifyRoleDto);
    }
    @PostMapping
    public ResponseResult<?> addRole(@RequestBody AddRoleDto addRoleDto) {
        return roleService.addRole(addRoleDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult<?> removeRole(@PathVariable("ids") String ids) {
        String[] idArray = ids.split(",");
        for (String str_id : idArray) {
            Long id = Long.parseLong(str_id);
            roleService.removeRole(id);
        }
        return ResponseResult.okResult();
    }
    @GetMapping("/listAllRole")
    public ResponseResult<?> getAllRole() {
        return roleService.getAllRole();
    }

}
