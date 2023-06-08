package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.entity.LoginUser;
import com.cdtu.domain.entity.User;
import com.cdtu.mapper.UserMapper;
import com.cdtu.service.MenuService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuService menuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        // 判断是否查到用户，没有查到就抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        // 返回用户信息
        // TODO: 角色权限
        // 前台不需要权限，后台需要权限
        if (user.getType().equals(SystemConstants.ADMIN_ROLE)) {
            // 查权限信息
            List<String> perms = menuService.getPermsByUserId(user.getId());
            return new LoginUser(user, perms);
        }
        return new LoginUser(user);
    }
}
