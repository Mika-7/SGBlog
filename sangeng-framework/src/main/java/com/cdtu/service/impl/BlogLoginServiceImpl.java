package com.cdtu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.LoginUser;
import com.cdtu.domain.entity.User;
import com.cdtu.domain.vo.BlogUserLoginVo;
import com.cdtu.domain.vo.user.UserInfoVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.mapper.UserMapper;
import com.cdtu.service.BlogLoginService;
import com.cdtu.utils.BeanCopyUtils;
import com.cdtu.utils.JwtUtil;
import com.cdtu.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service("blogLoginService")
public class BlogLoginServiceImpl extends ServiceImpl<UserMapper, User> implements BlogLoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    RedisCache redisCache;
    @Override
    public ResponseResult<?> login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断认证是否通过
        if(Objects.isNull(authenticate)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        // 获取 user id，生成 token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        // 把用户信息存入 redis
        redisCache.setCacheObject(SystemConstants.LOGIN_KEY_PREFIX +userId, loginUser);
        // 封装 token 和用户信息，返回响应
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult<?> logout() {
        // 获取 token，解析 user id
        Authentication authenticate = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 获取 user id
        Long userId = loginUser.getUser().getId();
        // 删除 redis 中还未过期的 token
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
