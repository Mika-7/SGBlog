package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddRoleDto;
import com.cdtu.domain.dto.ChangeStatusRoleDto;
import com.cdtu.domain.dto.ListRoleDto;
import com.cdtu.domain.dto.ModifyRoleDto;
import com.cdtu.domain.entity.Role;
import com.cdtu.domain.entity.RoleMenu;
import com.cdtu.domain.vo.*;
import com.cdtu.domain.vo.role.GetRoleVo;
import com.cdtu.domain.vo.role.RoleVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.mapper.RoleMapper;
import com.cdtu.service.MenuService;
import com.cdtu.service.RoleMenuService;
import com.cdtu.service.RoleService;
import com.cdtu.utils.BeanCopyUtils;
import com.cdtu.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author mika
 * @since 2023-05-18 17:00:04
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    RoleMenuService roleMenuService;
    @Resource
    MenuService menuService;
    @Override
    public List<String> getRoleKeyByUserId(Long userId) {
        List<String> rolesKeys = new ArrayList<>();
        // 判断是否是管理员，如果是返回集合中只需要 admin
        if (SecurityUtils.isAdmin()) {
            rolesKeys.add("admin");
            return rolesKeys;
        }
        // 否则，查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(userId);
    }

    @Override
    public ResponseResult<?> listRole(Integer pageNum, Integer pageSize, ListRoleDto listRoleDto) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        // 角色名称模糊查询
        queryWrapper.like(StringUtils.hasText(listRoleDto.getRoleName()), Role::getRoleName, listRoleDto.getRoleName());
        // 状态查询
        queryWrapper.eq(StringUtils.hasText(listRoleDto.getStatus()), Role::getStatus, listRoleDto.getStatus());
        // role_sort 升序排列
        //noinspection unchecked
        queryWrapper.orderByAsc(Role::getRoleSort);
        // 分页查询
        Page<Role> curPage = new Page<>(pageNum, pageSize);
        page(curPage, queryWrapper);
        // 转换 RoleVo
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(curPage.getRecords(), RoleVo.class);
        PageVo<RoleVo> pageVo = new PageVo<RoleVo>(roleVos, curPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> changeStatus(ChangeStatusRoleDto changeStatusRoleDto) {
        Role role = new Role();
        role.setId(changeStatusRoleDto.getRoleId());
        role.setStatus(changeStatusRoleDto.getStatus());
        // 改变 status 值
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        // 保存到 role 表
        save(role);
        Long roleId = role.getId();
        // 添加 menu_role 表的关系
        List<Long> menuIds = addRoleDto.getMenuIds();

        List<RoleMenu> roleMenus = menuIds.stream()
                .map(menuId -> new RoleMenu(roleId, menuId))
                .collect(Collectors.toList());

        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> removeRole(Long roleId) {
        // 不存在
        if (!isExist(roleId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        // 存在则，删除
        removeById(roleId);
        // 删除 roleMenu 表
        LambdaQueryWrapper<RoleMenu> queryWrapperRM = new LambdaQueryWrapper<>();
        queryWrapperRM.eq(RoleMenu::getRoleId, roleId);
        roleMenuService.remove(queryWrapperRM);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getRole(Long roleId) {
        // 不存在
        if (!isExist(roleId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        GetRoleVo getroleVo = BeanCopyUtils.copyBean(getById(roleId), GetRoleVo.class);
        return ResponseResult.okResult(getroleVo);
    }

    @Override
    @Transactional
    public ResponseResult<?> modifyRole(ModifyRoleDto modifyRoleDto) {
        Long roleId = modifyRoleDto.getId();
        // 不存在
        if (!isExist(roleId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        // 开始修改 role 表
        Role role = BeanCopyUtils.copyBean(modifyRoleDto, Role.class);
        updateById(role);
        // 开始修改 role_menu 表
        // 先删除原记录录
        roleMenuService.removeById(roleId);
        // 再新增
        List<Long> menuIds = modifyRoleDto.getMenuIds();
        List<RoleMenu> roleMenus = menuIds.stream()
                .map(menuId -> new RoleMenu(roleId, menuId))
                .collect(Collectors.toList());

        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);

        List<Role> roles = list(queryWrapper);
        return ResponseResult.okResult(roles);
    }

    private boolean isExist(Long roleId) {
        // 查看是否存在
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, roleId);
        int count = count(queryWrapper);
        return count > 0;
    }
}
