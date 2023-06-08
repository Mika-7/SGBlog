package com.cdtu.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}
    public static <V>V copyBean(Object source, Class<V> clazz) {
        // 创建对象
        V result;
        try {
            result = clazz.newInstance();
            // 实现属性拷贝
            BeanUtils.copyProperties(source, result);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public static <V>List<V> copyBeanList(List<?> list, Class<V> clazz) {
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
