package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.dto.AddArticleDto;
import com.cdtu.domain.dto.AdminArticleDto;
import com.cdtu.domain.dto.ListArticleDto;
import com.cdtu.domain.entity.Article;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.ArticleTag;
import com.cdtu.domain.entity.Category;
import com.cdtu.domain.vo.*;
import com.cdtu.domain.vo.article.*;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.mapper.ArticleMapper;
import com.cdtu.service.ArticleService;
import com.cdtu.service.ArticleTagService;
import com.cdtu.service.CategoryService;
import com.cdtu.utils.BeanCopyUtils;
import com.cdtu.utils.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 * note:
 * 当涉及到字段 viewCount 的获取，一般使用 this.setViewCountByRedis 方法
 * @author mika
 * @since 2023-05-11 11:30:32
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    CategoryService categoryService;
    @Resource
    ArticleTagService articleTagService;
    @Resource
    RedisCache redisCache;
    @Override
    public ResponseResult<?> hotArticleList() {
        // 获取热门文章列表,封装成 ResponseResult
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 正式文章 status 0
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 根据浏览量（view_count）降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        // 取10条
        Page<Article> curPage = new Page<>(1, 10);
        page(curPage, queryWrapper);
        List<Article> articles = curPage.getRecords();
        articles.stream()
                .peek(this::setViewCountByRedis);

        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult<?> articleList(Long categoryId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询条件
        // 若有 categoryId，就查对应分类文章
        articleLambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        // 状态是正常发布的
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 文章置顶：根据 is_top 降序排序
        articleLambdaQueryWrapper.orderByDesc(Article::getIsTop);

        // 分页查询
        Page<Article> curPage = new Page<>(pageNum, pageSize);
        page(curPage, articleLambdaQueryWrapper);
        // 多表查询
        List<Article> articles = curPage.getRecords();
        articles = articles.stream()
                .peek(article -> { // peek 用于调试，修改可变字段
                    Category category = categoryService.getById(article.getCategoryId());
                    article.setCategoryName(category.getName());
                    this.setViewCountByRedis(article);
                })
                .collect(Collectors.toList());

        // 封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        PageVo<Article> pageVo = new PageVo(articleListVos, curPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> getArticleDetail(Long id) {
        // 根据 id 查询文章
        Article article;
        try {
            article = getById(id);
        }
        catch (NullPointerException e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        // 从 redis 中查 viewCount
        setViewCountByRedis(article);

        // 转换成 vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //  根据分类 id 查询类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        // 封装 ResponseResult
        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 从 redis 中取值，修改浏览量
     * @param article 文章
     */
    private void setViewCountByRedis(Article article) {
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT_KEY_PREFIX, String.valueOf(article.getId()));
        article.setViewCount(Long.valueOf(viewCount));
    }

    @Override
    public ResponseResult<?> updateViewCount(Long id) {
        // 更新 redis 中 viewCount 值
        redisCache.incrementCacheMapValue(SystemConstants.VIEW_COUNT_KEY_PREFIX, id.toString(), 1);
        return ResponseResult.okResult();
    }
    // 加个事务
    @Override
    @Transactional
    public ResponseResult<?> addArticle(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        // 对 article 表，插入文章
        save(article);
        // 获取到插入的 ID
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        // 在 article_tag 表中添加一组关系
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> listArticle(Integer pageNum, Integer pageSize, ListArticleDto listArticleDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 标题和简介的模糊查询
        queryWrapper.like(Objects.nonNull(listArticleDto.getTitle()), Article::getTitle, listArticleDto.getTitle());
        queryWrapper.like(Objects.nonNull(listArticleDto.getSummary()), Article::getSummary, listArticleDto.getSummary());
        // 分页
        Page<Article> curPage = new Page<>(pageNum, pageSize);
        page(curPage, queryWrapper);
        // 转换 AdminArticleVo
        List<Article> articles = curPage.getRecords();
        articles.stream()
                .peek(this::setViewCountByRedis);

        List<AdminArticleVo> adminArticleVos = BeanCopyUtils.copyBeanList(articles, AdminArticleVo.class);
        // 转换为 PageVo
        PageVo<AdminArticleVo> pageVo = new PageVo<>(adminArticleVos, curPage.getTotal());
        // 封装返回
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> getAdminArticleDetail(Long id) {
        // 先查 article
        Article article;
        try {
            article = getById(id);
        }
        catch (NullPointerException e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        this.setViewCountByRedis(article);
        // 查询当前 article 的 tags 关系
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);

        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        List<Long> tags = articleTags.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());

        // 转换成 AdminArticleDetailVo
        AdminArticleDetailVo adminArticleDetailVo = BeanCopyUtils.copyBean(article, AdminArticleDetailVo.class);
        adminArticleDetailVo.setTags(tags);

        return ResponseResult.okResult(adminArticleDetailVo);
    }

    @Override
    @Transactional
    public ResponseResult<?> modifyArticle(AdminArticleDto adminArticleDto) {
        Article article = BeanCopyUtils.copyBean(adminArticleDto, Article.class);
        // 更新 article 表
        updateById(article);
        // 获取文章 id 和 tag id
        Long articleId = article.getId();
        List<Long> tags = adminArticleDto.getTags();
        List<ArticleTag> articleTags = new ArrayList<>();
        // 删除原来的文章对应的 tag
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, articleId);

        articleTagService.remove(queryWrapper);

        // 组装成实体
        tags.forEach(tagId -> {
                    articleTags.add(new ArticleTag(articleId, tagId));
                });
        // 保存 article_tag 表
        // 有一个小问题：如果没有在 ArticleTag 实体中指定主键为 INPUT 类型
        //               那么，mybatisPlus 将忽略已指定的值，按数据库的自增策略设定主键 Id
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> removeArticle(Long id) {
        // 查看是否存在该文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        int count = count(queryWrapper);
        if (count <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        // 存在则，删除
        removeById(id);
        return ResponseResult.okResult();
    }
}

