package com.cdtu.aspect;

import com.alibaba.fastjson.JSON;
import com.cdtu.anotation.SystemLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 切面增强类
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut(" @annotation(com.cdtu.anotation.SystemLog)")
    public void pt() {

    }
    @SneakyThrows
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) {
        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
            log.info("============End============" + System.lineSeparator());
        }
        return ret;
    }

    private void handleAfter(Object ret) {
        // 打印响应结果
        log.info("Response       : {}", JSON.toJSONString(ret));
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        // 获取被增强方法的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);
        Class declaringType = joinPoint.getSignature().getDeclaringType();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        log.info("===========Start===========");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", declaringType.getName(), method.getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
        // 打印出参
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(SystemLog.class);
    }

}

