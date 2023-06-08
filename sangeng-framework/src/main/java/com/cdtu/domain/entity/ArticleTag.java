package com.cdtu.domain.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author mika
 * @since 2023-05-21 12:14:42
 */
//@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_article_tag")
public class ArticleTag  implements Serializable {
    private static final long serialVersionUID = -4647454052401493532L;
    //文章id
    @TableId(type = IdType.INPUT)
    private Long articleId;
    //标签id
    private Long tagId;
}
