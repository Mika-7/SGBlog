package com.cdtu.job;

import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.entity.Article;
import com.cdtu.service.ArticleService;
import com.cdtu.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleService articleService;
    // cron 表达式：秒 分 时 日 月 周几 年(可省略)
//    @Scheduled(cron =  "* 0/10 * * * ?")
    @Scheduled(cron =  "0/5 * * * * ?")
    public void updateViewCount() {
        // 把 redis 中的浏览量同步到 Mysql
        System.out.println("定时任务执行:更新 Mysql 数据");
        // 获取 redis 中的浏览量
        Map<Object, Object> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT_KEY_PREFIX);
        List<Article>  articles = viewCountMap.entrySet().stream()
                // 第一 key 为 String 类型，value 为 Integer
                .map(entry -> new Article(Long.valueOf((String) entry.getKey()), Long.valueOf((Integer) entry.getValue())))
                .collect(Collectors.toList());

        // 更新到 Mysql 数据库中
        articleService.updateBatchById(articles);

    }
}
