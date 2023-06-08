package com.cdtu.domain.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 开启链式调用
public class UserInfoVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别
     */
    private String sex;

    private String email;
}
