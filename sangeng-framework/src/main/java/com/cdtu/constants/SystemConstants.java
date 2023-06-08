package com.cdtu.constants;

public class SystemConstants {
    /**
     * 文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     * 文章是已发布
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     * 分类状态正常
     */
    public static final String STATUS_NORMAL = "0";
    /**
     * 友链状态审核通过
     */
    public static final String LINK_STATUS_NORMAL = "0";
    /**
     * 根评论，代表评论区第一层的留言
     */
    public static final int COMMENT_IS_ROOT = -1;
    /**
     * 没有回复对象
     */
    public static final int COMMENT_NO_OBJECT = -1;
    /**
     * 评论类型：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型：友链评论
     */
    public static final String LINK_COMMENT = "1";
    /**
     * 浏览量 Key 前缀
     */
    public static final String VIEW_COUNT_KEY_PREFIX = "article:viewCount";
    /**
     * 博客登录 Key 前缀
     */
    public static final String LOGIN_KEY_PREFIX = "bloglogin:";
    /**
     * 后台登录 Key 前缀
     */
    public static final String ADMIN_KEY_PREFIX = "adminlogin:";
    /**
     * 菜单状态：正常
     */
    public static final String MENU_STATUS_NORMAL = "0";
    public static final String CATEGORY_STATUS_NORMAL = "0";
    public static final String IMAGE_SUFFIX = "\\.(png|jpg)$";
    public static final String ADMIN_ROLE = "1";
}

