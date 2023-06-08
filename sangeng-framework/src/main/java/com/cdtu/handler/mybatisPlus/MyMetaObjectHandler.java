package com.cdtu.handler.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cdtu.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mybatis-Plus 的自动填充
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Long userId = getUserId(metaObject);

        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        Long userId = getUserId(metaObject);

        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }
    private Long getUserId(MetaObject metaObject) {
        Long userId;
        try {
            userId = SecurityUtils.getUserId();
        }
        catch (Exception e) {
//            e.printStackTrace();
            // 表示是自己创建的
            log.info("更新数据库 ===>  数据来源：自己创建的");
            userId = (Long) metaObject.getValue("id");
        }
        return userId;
    }
}