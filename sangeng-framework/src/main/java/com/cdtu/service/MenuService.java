package com.cdtu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.Menu;
import com.cdtu.domain.vo.menu.MenuTreeSelectVo;
import com.cdtu.domain.vo.menu.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author mika
 * @since 2023-05-18 16:51:06
 */
public interface MenuService extends IService<Menu> {

    List<String> getPermsByUserId(Long userId);

    List<MenuVo> selectMenuVoTreeByUserId(Long userId);

    ResponseResult<?> listMenu(String status, String menuName);

    ResponseResult<?> addMenu(Menu menu);

    ResponseResult<?> getMenu(Long id);

    ResponseResult<?> modifyMenu(Menu menu);

    ResponseResult<?> removeMenu(Long id);

    ResponseResult<?> getTreeSelect();

    List<MenuTreeSelectVo> getMenuTree(Long menuId);
    ResponseResult<?> getRoleMenuTreeSelect(Long roleId);
}
