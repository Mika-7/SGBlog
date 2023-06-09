package com.cdtu.domain.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailInfoVo {
    private Long id;
    //用户名
    private String userName;
    private String nickName;
    //账号状态（0正常 1停用）
    private String status;
    //邮箱
    private String email;
    //用户性别（0男，1女，2未知）
    private String sex;
}
