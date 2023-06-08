package com.cdtu.domain.vo.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticleVo {
    private Long id;
    // 标题
    private String title;
    // 访问数
    private Long viewCount;
}
