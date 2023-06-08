package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.service.ArticleService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/article")
@Api(tags = "文章", description = "文章相关接口")
public class ArticleController {
    @Resource
    ArticleService articleService;
    @GetMapping("/hotArticleList")
    @ApiOperation(value = "热门文章", notes = "获取热门文章")
    public ResponseResult<?> hotArticle() {
        return (ResponseResult<?>) articleService.hotArticleList();
    }
    @GetMapping("/articleList")
    public ResponseResult<?> articleList(
            Long categoryId,
            @RequestParam("pageNum") Integer pageNum, // @RequestParam 注解会要求参数不能为空，否则报 400 错误
            @RequestParam("pageSize")Integer pageSize) {
        return  articleService.articleList(categoryId, pageNum, pageSize);
    }
    @GetMapping("/{id}")
    @ApiOperation(value = "查看文章", notes = "查看文章详情")
    @ApiImplicitParam(name = "id", value = "待查看文章 ID")
    public ResponseResult<?> getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }
    @PutMapping("/updateViewCount/{id}")
    @ApiOperation(value = "更新浏览量", notes = "更新文章的浏览量")
    @ApiImplicitParam(name = "id", value = "待更新文章 ID")
    public ResponseResult<?> updateViewCount(@PathVariable("id")Long id) {
        return articleService.updateViewCount(id);
    }

}
