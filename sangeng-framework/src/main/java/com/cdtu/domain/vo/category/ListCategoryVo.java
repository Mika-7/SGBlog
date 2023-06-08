package com.cdtu.domain.vo.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListCategoryVo {
    private String name;
    private String status;
    private Long id;
    private String description;
}
