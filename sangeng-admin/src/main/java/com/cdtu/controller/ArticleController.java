package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddArticleDto;
import com.cdtu.domain.dto.AdminArticleDto;
import com.cdtu.domain.dto.ListArticleDto;
import com.cdtu.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    ArticleService articleService;
    @PostMapping
    public ResponseResult<?> addArticle(@RequestBody AddArticleDto addArticleDto) {
        return articleService.addArticle(addArticleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult<?> getArticleDetail(@PathVariable("id")Long id) {
        return articleService.getAdminArticleDetail(id);
    }
    @GetMapping("/list")
    public ResponseResult<?> listArticle(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize,
            ListArticleDto listArticleDto) {
        return articleService.listArticle(pageNum, pageSize, listArticleDto);
    }
    @PutMapping
    public ResponseResult<?> modifyArticle(@RequestBody AdminArticleDto adminArticleDto) {
        return articleService.modifyArticle(adminArticleDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult<?> removeArticle(@PathVariable("ids") String ids) {
        String[] idArray = ids.split(",");
        for (String str_id : idArray) {
            Long id = Long.parseLong(str_id);
            articleService.removeArticle(id);
        }
        return ResponseResult.okResult();
    }
}
