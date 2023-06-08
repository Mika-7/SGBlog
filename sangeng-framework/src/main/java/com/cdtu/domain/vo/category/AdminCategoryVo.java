package com.cdtu.domain.vo.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCategoryVo {
    private Long id;
    private String name;
    private String description;
}