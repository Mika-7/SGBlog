package com.cdtu.runner;

import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.entity.Article;
import com.cdtu.mapper.ArticleMapper;
import com.cdtu.service.ArticleService;
import com.cdtu.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        // 应用启动时，把博客浏览量存到 redis 中
        List< Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(
                        article -> article.getId().toString(),
                        article -> article.getViewCount().intValue()
                ));

        redisCache.setCacheMap(SystemConstants.VIEW_COUNT_KEY_PREFIX, viewCountMap);
    }
}
