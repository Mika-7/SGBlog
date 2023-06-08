package com.cdtu.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.LoginUser;
import com.cdtu.domain.entity.User;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.utils.JwtUtil;
import com.cdtu.utils.RedisCache;
import com.cdtu.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Jwt Token 校验类
 * 如果 token 失效，就会提示重新登录
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的 token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 说明该接口不需要登录，直接放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析拿到 user id
        Claims claims = null;
        User user = null;
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = Objects.requireNonNull(claims).getSubject();
            // 拿到 redis 中 的用户信息
            JSONObject jsonObject = redisCache.getCacheObject(SystemConstants.ADMIN_KEY_PREFIX + userId);
            // 转换成 LoginUser 对象
            user = jsonObject.getJSONObject("user").toJavaObject(User.class);
        } catch (Exception e) {
            e.printStackTrace();
            // token 超时，或者 token 不存在
            // 响应：需要重新登录
            ResponseResult<?> result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        LoginUser loginUser = new LoginUser(user);
        // 存入到 SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
