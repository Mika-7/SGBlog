package com.cdtu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.domain.entity.RoleMenu;
import com.cdtu.mapper.RoleMenuMapper;
import com.cdtu.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author mika
 * @since 2023-05-25 21:22:01
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
