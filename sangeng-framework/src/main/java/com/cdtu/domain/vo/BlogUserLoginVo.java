package com.cdtu.domain.vo;

import com.cdtu.domain.vo.user.UserInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogUserLoginVo {
    private String token;
    private UserInfoVo userInfo;
}
