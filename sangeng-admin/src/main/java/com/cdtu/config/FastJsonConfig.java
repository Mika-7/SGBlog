package com.cdtu.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastJsonConfig {
    static {
        // 关闭默认 ref 引用，直接显示 JSON 内容
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }
}
