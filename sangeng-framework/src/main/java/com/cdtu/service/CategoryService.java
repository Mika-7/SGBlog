package com.cdtu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddCategoryDto;
import com.cdtu.domain.dto.ListCategoryDto;
import com.cdtu.domain.dto.ModifyCategory;
import com.cdtu.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author mika
 * @since 2023-05-11 23:16:35
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult<?> listAllCategory();

    ResponseResult<?> getListCategory(Integer pageNum, Integer pageSize, ListCategoryDto listCategoryDto);

    ResponseResult<?> addCategory(AddCategoryDto addCategoryDto);

    ResponseResult<?> getCategoryById(Long id);

    ResponseResult<?> modifyCategory(ModifyCategory modifyCategory);

    ResponseResult<?> removeCategory(Long id);
}
