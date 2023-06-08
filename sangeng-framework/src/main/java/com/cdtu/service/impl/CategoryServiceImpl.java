package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddCategoryDto;
import com.cdtu.domain.dto.ListCategoryDto;
import com.cdtu.domain.dto.ModifyCategory;
import com.cdtu.domain.entity.Article;
import com.cdtu.domain.entity.Category;
import com.cdtu.domain.vo.category.ListCategoryVo;
import com.cdtu.domain.vo.PageVo;
import com.cdtu.domain.vo.category.AdminCategoryVo;
import com.cdtu.domain.vo.category.CategoryVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.mapper.CategoryMapper;
import com.cdtu.service.ArticleService;
import com.cdtu.service.CategoryService;
import com.cdtu.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.annotation.Repeatable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author mika
 * @since 2023-05-11 23:16:35
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper  = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 查询文章表，状态为已发布
        List<Article> articles = articleService.list(articleLambdaQueryWrapper);
        // 获取文章的分类 id，并去重
        Set<Long> categoryIds = articles.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        // 查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        // 转换成 vo，返回
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
    // 写博文用的
    @Override
    public ResponseResult<?> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL);

        List<Category> categories = list(queryWrapper);
        List<AdminCategoryVo> adminCategoryVos = BeanCopyUtils.copyBeanList(categories, AdminCategoryVo.class);

        return ResponseResult.okResult(adminCategoryVos);
    }

    @Override
    public ResponseResult<?> getListCategory(Integer pageNum, Integer pageSize, ListCategoryDto listCategoryDto) {
        String name = listCategoryDto.getName();
        String status = listCategoryDto.getStatus();
        // 查询条件：
        //          能根据分类名称进行get模糊查询。
        //          能根据状态进行查询。
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Category::getName, name);
        queryWrapper.eq(StringUtils.hasText(status), Category::getStatus, status);
        // 分页
        Page<Category> curPage = new Page<>(pageNum, pageSize);
        page(curPage, queryWrapper);
        List<ListCategoryVo> listCategoryVos = BeanCopyUtils.copyBeanList(curPage.getRecords(), ListCategoryVo.class);
        // 封装成 pageVo 返回
        PageVo<ListCategoryVo> pageVo = new PageVo<>(listCategoryVos, curPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getCategoryById(Long id) {
        if (!categoryExist(id)) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        Category category = getById(id);
        ListCategoryVo listCategoryVo = BeanCopyUtils.copyBean(category, ListCategoryVo.class);
        return ResponseResult.okResult(listCategoryVo);
    }

    @Override
    public ResponseResult<?> modifyCategory(ModifyCategory modifyCategory) {
        if (!categoryExist(modifyCategory.getId())) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        // 转换为实体
        Category category = BeanCopyUtils.copyBean(modifyCategory, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> removeCategory(Long id) {
        if (!categoryExist(id)) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    private boolean categoryExist(Long id) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, id);
        return count(queryWrapper) > 0;
    }
}
