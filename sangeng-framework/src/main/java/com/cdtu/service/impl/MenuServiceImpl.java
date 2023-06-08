package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.Menu;
import com.cdtu.domain.entity.RoleMenu;
import com.cdtu.domain.vo.*;
import com.cdtu.domain.vo.menu.GetMenuVo;
import com.cdtu.domain.vo.menu.ListMenuVo;
import com.cdtu.domain.vo.menu.MenuTreeSelectVo;
import com.cdtu.domain.vo.menu.MenuVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.enums.MenuTypeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.mapper.MenuMapper;
import com.cdtu.service.MenuService;
import com.cdtu.service.RoleMenuService;
import com.cdtu.utils.BeanCopyUtils;
import com.cdtu.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author mika
 * @since 2023-05-18 16:51:06
 */
@Service("menuService")
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    RoleMenuService roleMenuService;
    @Override
    public List<String> getPermsByUserId(Long userId) {
        // 若是管理员，返回所有权限
        if (SecurityUtils.isAdmin(userId)) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.in(Menu::getMenuType, MenuTypeEnum.BUTTON.getType(), MenuTypeEnum.MENU.getType());
            queryWrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);

            List<Menu> list = list(queryWrapper);
            List<String> perms = list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        // 否则返回其所具有的权限
        return getBaseMapper().selectPermsByUserId(userId);
    }

    @Override
    public List<MenuVo> selectMenuVoTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus;
        // 判读是否是管理员，若是，返回所有符合要求的
        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectAllRouterMenu();
        }
        // 不是管理员，返回当前所具有的 menu （C 和 M 类型）
        else {
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        List<MenuVo> menuVoTree = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        // 填充 children 的值
        menuVoTree = fillChildren(menuVoTree, 0L);

        return menuVoTree;
    }

    @Override
    public ResponseResult<?> listMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // status 查询
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        // menuName 模糊查询
        queryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        // 按照 parentId，orderNum 排序
        //noinspection unchecked
        queryWrapper.orderByAsc(Menu::getParentId);
        //noinspection unchecked
        queryWrapper.orderByAsc(Menu::getOrderNum);
        // 转换为 ListMenuVo
        List<Menu> menus = list(queryWrapper);
        List<ListMenuVo> listMenuVos = BeanCopyUtils.copyBeanList(menus, ListMenuVo.class);
        return ResponseResult.okResult(listMenuVos);
    }

    @Override
    public ResponseResult<?> addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getMenu(Long id) {
        Menu menu = getById(id);
        if (Objects.isNull(menu)) {
            log.error("Menu 菜单为空");
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        GetMenuVo getMenuVo = BeanCopyUtils.copyBean(menu, GetMenuVo.class);
        return ResponseResult.okResult(getMenuVo);
    }

    @Override
    public ResponseResult<?> modifyMenu(Menu menu) {
        Long id = menu.getId();
        // 判断是否存在于数据库
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, id);
        int count = count(queryWrapper);
        if (count <= 0) {
            log.error("修改的菜单不存在");
            return ResponseResult.errorResult(AppHttpCodeEnum.MENU_NO_EXIST);
        }
        // parentId 不能为自己
        if (menu.getParentId().equals(id)) {
            log.error(AppHttpCodeEnum.PARENT_ID_CANNOT_SELF.getMsg());
            return ResponseResult.errorResult(AppHttpCodeEnum.PARENT_ID_CANNOT_SELF);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> removeMenu(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        // 判断是否有子菜单
        int count = count(queryWrapper);
        if (count > 0) {
            // 存在子菜单，删除失败，不允许删除
            log.error(AppHttpCodeEnum.EXIST_CHILD_MENU.getMsg());
            return ResponseResult.errorResult(AppHttpCodeEnum.EXIST_CHILD_MENU);
        }
        // 进行删除
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getTreeSelect() {
        List<MenuTreeSelectVo> menuTreeSelectVos = getAllMenuTree();

        return ResponseResult.okResult(menuTreeSelectVos);
    }

    @Override
    public List<MenuTreeSelectVo> getMenuTree(Long menuId) {
        List<MenuTreeSelectVo> menuTreeSelectVos = getAllMenuTree();
        menuTreeSelectVos = menuTreeSelectVos.stream()
                                .filter(menu -> menuId.equals(menu.getId()))
                                .collect(Collectors.toList());
        return menuTreeSelectVos;
    }
    @Override
    public ResponseResult<?> getRoleMenuTreeSelect(Long roleId) {
        // role_id 对应的 menu_id
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = roleMenuService.list(queryWrapper);
        // 只获取 menu_id 列表
        List<Long> menuIds = roleMenus.stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());

        // 加载role 对应的 menu 菜单树
        RoleMenuTreeSelectVo roleMenuTreeSelectVo;
        List<MenuTreeSelectVo> allMenuTree = getAllMenuTree();
        // 当菜单 Id 为空, 代表没有权限
        // 返回全部，以及空的菜单
        if (menuIds.isEmpty()) {
            roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(allMenuTree, Collections.emptyList());
        }
        else {
            roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(allMenuTree, menuIds);
        }
        return ResponseResult.okResult(roleMenuTreeSelectVo);
    }


    private List<MenuTreeSelectVo> getAllMenuTree() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //noinspection unchecked
        queryWrapper.orderByAsc(Menu::getParentId);
        //noinspection unchecked
        queryWrapper.orderByAsc(Menu::getOrderNum);
        //  先查第一层的数据
        List<Menu> menus = list(queryWrapper);
        List<MenuTreeSelectVo> menuTreeSelectVos = menus.stream()
                .map(menu -> {
                    MenuTreeSelectVo menuTreeSelectVo = new MenuTreeSelectVo();
                    menuTreeSelectVo.setId(menu.getId());
                    menuTreeSelectVo.setParentId(menu.getParentId());
                    menuTreeSelectVo.setLabel(menu.getMenuName());
                    return menuTreeSelectVo;
                })
                .collect(Collectors.toList());

        // 然后再递归调用查 children 值
        menuTreeSelectVos = fillChildrenTree(menuTreeSelectVos, 0L);
        return menuTreeSelectVos;
    }
    /**
     * 为 MenuVo 列表对象填充未设置值的 children
     * @param menuVos
     * @param parentId
     * @return
     */
    private List<MenuVo> fillChildren(List<MenuVo> menuVos, Long parentId) {
        return menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .peek(menuVo -> {
                    List<MenuVo> children = getChildren(menuVo, menuVos);
                    menuVo.setChildren(children);
                })
                .collect(Collectors.toList());
    }
    private List<MenuTreeSelectVo> fillChildrenTree(List<MenuTreeSelectVo> menuVos, Long parentId) {
        return menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .peek(menuVo -> {
                    List<MenuTreeSelectVo> children = getChildren(menuVo, menuVos);
                    menuVo.setChildren(children);
                })
                .collect(Collectors.toList());
    }
    /**
     * 获取到 children 列表
     * id 与 parentId 是一对多的关系
     * @param menuVo 父节点
     * @param menuVos 待过滤的子节点列表
     * @return children 列表
     */
    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menuVos) {
        return menuVos.stream()
                .filter(m -> menuVo.getId().equals(m.getParentId()))
                .peek(m -> m.setChildren(getChildren(m, menuVos)))
                .collect(Collectors.toList());
    }
    private List<MenuTreeSelectVo> getChildren(MenuTreeSelectVo tree, List<MenuTreeSelectVo> trees) {
        return trees.stream()
                .filter(t -> tree.getId().equals(t.getParentId()))
                .peek(t -> t.setChildren(getChildren(t, trees)))
                .collect(Collectors.toList());
    }
}
