package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
@Api(tags = "分类", description = "分类相关接口")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @GetMapping("/getCategoryList")
    @ApiOperation(value = "获取分类", notes = "获取所有分类")
    public ResponseResult<?> getCategoryList() {
        return categoryService.getCategoryList();
    }
}
