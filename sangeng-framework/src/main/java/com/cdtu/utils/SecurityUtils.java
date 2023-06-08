package com.cdtu.utils;

import com.cdtu.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security 工具类
 */
public class SecurityUtils {
    /**
     * 获取到认证中的登录用户
     * @return 登录用户
     */
    public static LoginUser getLoginUser() {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取权限认证包装类
     * @return 认证类
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    /**
     * 获取用户 id
     */
    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
    public static boolean isAdmin() {
        Long id = getUserId();
        return id != null && id.equals(1L);
    }
    public static boolean isAdmin(Long userId) {
        return userId != null && userId.equals(1L);
    }
}
