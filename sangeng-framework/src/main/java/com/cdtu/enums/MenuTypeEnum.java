package com.cdtu.enums;

/**
 * 菜单类型
 */
public enum MenuTypeEnum {
    DIRECTORY("目录", "M"),
    MENU("菜单", "C"),
    BUTTON("按钮", "F");

    MenuTypeEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    private final String name;
    private final String type;

    public String getType() {
        return type;
    }
}
