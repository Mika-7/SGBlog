package com.cdtu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.domain.entity.UserRole;
import com.cdtu.mapper.UserRoleMapper;
import com.cdtu.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author mika
 * @since 2023-05-26 23:26:53
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
