package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.User;
import com.cdtu.domain.vo.user.AdminUserInfoVo;
import com.cdtu.domain.vo.menu.MenuVo;
import com.cdtu.domain.vo.RoutersVo;
import com.cdtu.domain.vo.user.UserInfoVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.service.AdminLoginService;
import com.cdtu.service.MenuService;
import com.cdtu.service.RoleService;
import com.cdtu.utils.BeanCopyUtils;
import com.cdtu.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AdminLoginController {
    @Resource
    AdminLoginService adminLoginService;
    @Resource
    RoleService roleService;
    @Resource
    MenuService menuService;
    @PostMapping("/user/login")
    public ResponseResult<?> login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }
    @PostMapping("/user/logout")
    public ResponseResult<?> logout() {
        return adminLoginService.logout();
    }
    @GetMapping("/getInfo")
    public ResponseResult<?> getInfo() {
        Long userId = SecurityUtils.getUserId();
        // 在 SecurityUtils 中获取 User 封装成 UserVo
        User user = SecurityUtils.getLoginUser().getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        // 根据用户 id 查询权限信息
        List<String> perms = menuService.getPermsByUserId(userId);
        // 根据用户 id 查询角色信息
        List<String> roles = roleService.getRoleKeyByUserId(userId);

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roles, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult<?> getRouters() {
        Long userId = SecurityUtils.getUserId();
        // 查询 MenuVo
        List<MenuVo> menuVos = menuService.selectMenuVoTreeByUserId(userId);
        RoutersVo routersVo = new RoutersVo(menuVos);
        return ResponseResult.okResult(routersVo);
    }
}
