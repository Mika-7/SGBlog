package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddUserDto;
import com.cdtu.domain.dto.ChangeStatusUserDto;
import com.cdtu.domain.dto.ListUserDto;
import com.cdtu.domain.dto.ModifyUserDto;
import com.cdtu.domain.entity.Role;
import com.cdtu.domain.entity.User;
import com.cdtu.domain.entity.UserRole;
import com.cdtu.domain.vo.*;
import com.cdtu.domain.vo.user.ListUserVo;
import com.cdtu.domain.vo.user.UserDetailInfoVo;
import com.cdtu.domain.vo.user.UserDetailVo;
import com.cdtu.domain.vo.user.UserInfoVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.mapper.UserMapper;
import com.cdtu.service.RoleService;
import com.cdtu.service.UserRoleService;
import com.cdtu.service.UserService;
import com.cdtu.utils.BeanCopyUtils;
import com.cdtu.utils.SecurityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author mika
 * @since 2023-05-13 09:59:15
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    BCryptPasswordEncoder passwordEncoder;
    @Resource
    UserRoleService userRoleService;
    @Resource
    RoleService roleService;
    @Override
    public ResponseResult<?> getUserInfo() {
        // 获取用户 id
        Long userId = SecurityUtils.getUserId();
        // 根据用户 id 查询用户信息
        User user = getById(userId);
        // 封装 UserVo 返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult<?> updateUserInfo(User user) {
        // 更新数据库用户信息
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> register(User user) {
        // 对数据进行判空
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        // 对数据是否在数据库有重复
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // ...
        // 密码加密处理
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // 存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getListUser(Integer pageNum, Integer pageSize, ListUserDto listUserDto) {
        String userName = listUserDto.getUserName();
        String phone = listUserDto.getPhonenumber();
        String status = listUserDto.getStatus();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName), User::getUserName, userName);
        queryWrapper.eq(StringUtils.hasText(phone), User::getPhonenumber, phone);
        queryWrapper.eq(StringUtils.hasText(status), User::getStatus, status);
        // 分页
        Page<User> curPage = new Page<>(pageNum, pageSize);
        page(curPage, queryWrapper);
        // 转 Vo
        List<ListUserVo> listUserVos = BeanCopyUtils.copyBeanList(curPage.getRecords(), ListUserVo.class);
        PageVo<ListUserVo> pageVo = new PageVo<>(listUserVos, curPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult<?> addUser(AddUserDto addUserDto) {
        // 转换成 User 对象保存到 user 表
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        checkUser(user);
        // 加密
        String encodedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPwd);
        save(user);
        // roleIds
        List<Long> roleIds = addUserDto.getRoleIds();
        // 添加 user_role 表 关系
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> removeUser(Long userId) {
        // 当前用户
        Long curUserId = SecurityUtils.getUserId();
        if (!userExist(userId)) {
            throw new SystemException(AppHttpCodeEnum.USER_NOT_EXIST);
        } else if (userId.equals(curUserId)) {
            throw new SystemException(AppHttpCodeEnum.CANNOT_REMOVE_CURRENT_USER);
        }
        removeById(userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getUserDetailById(Long userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        // 获取当前用户所具有的角色列表 roleIds
        List<UserRole> userRoles = userRoleService.list(queryWrapper);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        // 获取所有的 roles
        List<Role> roles = roleService.list();
        // 获取当前用户信息，封装成 UserDetailInfoVo
        User user = getById(userId);
        UserDetailInfoVo userDetailInfoVo = BeanCopyUtils.copyBean(user, UserDetailInfoVo.class);
        // 最终封装的 Vo
        UserDetailVo userDetailVo = new UserDetailVo(roleIds, roles, userDetailInfoVo);
        return ResponseResult.okResult(userDetailVo);
    }

    @Override
    @Transactional
    public ResponseResult<?> modifyUser(ModifyUserDto modifyUserDto) {
        User user = BeanCopyUtils.copyBean(modifyUserDto, User.class);
        Long userId = user.getId();
        // 判断是否存在
        if (!userExist(userId)) throw new SystemException(AppHttpCodeEnum.USER_NOT_EXIST);
        // 更新 user 表
        updateById(user);
        // 更新 user_role 表
        userRoleService.removeById(userId);
        List<Long> roleIds = modifyUserDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> new UserRole(userId, roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> changeStatus(ChangeStatusUserDto changeStatusUserDto) {
        User user = BeanCopyUtils.copyBean(changeStatusUserDto, User.class);
        user.setId(changeStatusUserDto.getUserId());
        if (!userExist(user.getId())) throw new SystemException(AppHttpCodeEnum.USER_NOT_EXIST);
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean userExist(Long userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        return count(queryWrapper) > 0;
    }
    private void checkUser(User user) throws SystemException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//      用户名为空，提示：必需填写用户名
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        // 用户名不能为空
        else {
//	        用户名必须之前未存在，否则提示：用户名已存在
            if (userNameExist(user.getUserName())) throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
//      手机号必须之前未存在，否则提示：手机号已存在
        if (phoneExist(user.getPhonenumber())) throw new SystemException(AppHttpCodeEnum.PHONE_NUMBER_EXIST);
//	    邮箱必须之前未存在，否则提示：邮箱已存在
        if (emailExist(user.getEmail())) throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
    }


    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper) > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        return count(queryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper) > 0;
    }

    private boolean phoneExist(String phoneNumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phoneNumber);
        return count(queryWrapper) > 0;
    }
}
