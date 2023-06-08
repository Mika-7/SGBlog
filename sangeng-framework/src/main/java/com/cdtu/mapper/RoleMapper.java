package com.cdtu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdtu.domain.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author mika
 * @since 2023-05-18 17:00:04
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(@Param("userId") Long userId);
}

