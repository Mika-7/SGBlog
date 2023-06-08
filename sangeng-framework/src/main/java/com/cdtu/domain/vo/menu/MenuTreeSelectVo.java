package com.cdtu.domain.vo.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeSelectVo {
    private List<MenuTreeSelectVo> children;
    private Long id;
    private String label;
    private Long parentId;
}
