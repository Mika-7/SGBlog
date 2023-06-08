package com.cdtu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdtu.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author mika
 * @since 2023-05-18 16:51:06
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}

