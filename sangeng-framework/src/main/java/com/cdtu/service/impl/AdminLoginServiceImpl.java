package com.cdtu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.LoginUser;
import com.cdtu.domain.entity.User;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.mapper.UserMapper;
import com.cdtu.service.AdminLoginService;
import com.cdtu.utils.JwtUtil;
import com.cdtu.utils.RedisCache;
import com.cdtu.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service("adminLoginService")
public class AdminLoginServiceImpl extends ServiceImpl<UserMapper, User> implements AdminLoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Override
    public ResponseResult<?> login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        // 调用 ProvideManager 进行认证登录
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        // 通过实现了 UserDetails 接口 的实现类，获取 id
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        // 通过认证，生成 jwt token 存到 redis 中
        String jwt = JwtUtil.createJWT(userId.toString());
        redisCache.setCacheObject(SystemConstants.ADMIN_KEY_PREFIX + userId, loginUser);
        // 响应 token
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult<?> logout() {
        Long userId = SecurityUtils.getUserId();
//        if (Objects.isNull(userId)) {
//            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
//        }
        // 删除 redis 中的登录 token 凭证
        redisCache.deleteObject(SystemConstants.ADMIN_KEY_PREFIX + userId);

        return ResponseResult.okResult();
    }
}
