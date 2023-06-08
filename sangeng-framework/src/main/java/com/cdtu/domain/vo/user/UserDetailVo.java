package com.cdtu.domain.vo.user;

import com.cdtu.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserDetailInfoVo user;
}
