package com.cdtu.enums;

/**
 * 前端响应自定义状态码
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONE_NUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506, "内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    MENU_NO_EXIST(513, "菜单不存在"),
    PARENT_ID_CANNOT_SELF("上级菜单不能选择自己"),
    EXIST_CHILD_MENU("存在子菜单不允许删除"),
    USER_NOT_EXIST("用户不存在"),
    CANNOT_REMOVE_CURRENT_USER("不能删除当前用户"),
    CATEGORY_NOT_EXIST("分类不存在"),
    LINK_NOT_EXIST("友链不存在");

    int code;
    String msg;

    AppHttpCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    AppHttpCodeEnum(String msg) {
        this.code = 500;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
