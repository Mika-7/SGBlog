package com.cdtu.domain.vo;

import com.cdtu.domain.vo.menu.MenuTreeSelectVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeSelectVo {
    private List<MenuTreeSelectVo> menus;
    private List<Long> checkedKeys;
}
