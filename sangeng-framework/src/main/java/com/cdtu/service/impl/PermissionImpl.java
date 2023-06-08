package com.cdtu.service.impl;

import com.cdtu.service.PermissionService;
import com.cdtu.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
@Slf4j
public class PermissionImpl implements PermissionService {
    /**
     * 判断当前用户是否具有 permission
     * @param permission 需要判断的权限
     * @return 是否具有权限 boolean 值
     */
    @Override
    public boolean hasPermission(String permission) {
        // 如果是超级管理员，直接返回 true
        if (SecurityUtils.isAdmin()) return true;
        // 否则，获取当前登录用户所具有的权限列表
        else {
            List<String> permissions;
            boolean isContain;
            try {
                permissions = SecurityUtils.getLoginUser().getPermissions();
                isContain = permissions.contains(permission);
            }
            catch (NullPointerException e) {
//                e.printStackTrace();
                log.warn("没有任何权限的用户访问");
                return false;
            }
            return isContain;
        }
    }
}
