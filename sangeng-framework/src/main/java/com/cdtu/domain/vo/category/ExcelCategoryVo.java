package com.cdtu.domain.vo.category;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ExcelCategoryVo {
    @ExcelProperty("分类名称")
    private String name;
    @ExcelProperty("描述")
    private String description;
    @ExcelProperty("状态（0：正常，1：禁用）")
    private String status;
}
