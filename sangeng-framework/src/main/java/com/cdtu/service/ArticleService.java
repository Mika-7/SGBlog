package com.cdtu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.domain.dto.AddArticleDto;
import com.cdtu.domain.dto.AdminArticleDto;
import com.cdtu.domain.dto.ListArticleDto;
import com.cdtu.domain.entity.Article;
import com.cdtu.domain.ResponseResult;

/**
 * 文章表(Article)表服务接口
 *
 * @author mika
 * @since 2023-05-11 11:30:31
 */
public interface ArticleService extends IService<Article> {

    ResponseResult<?> hotArticleList();

    ResponseResult<?> articleList(Long categoryId, Integer pageNum, Integer pageSize);

    ResponseResult<?> getArticleDetail(Long id);

    ResponseResult<?> updateViewCount(Long id);

    ResponseResult<?> addArticle(AddArticleDto addArticleDto);

    ResponseResult<?> listArticle(Integer pageNum, Integer pageSize, ListArticleDto listArticleDto);

    ResponseResult<?> getAdminArticleDetail(Long id);

    ResponseResult<?> modifyArticle(AdminArticleDto adminArticleDto);

    ResponseResult<?> removeArticle(Long id);
}

