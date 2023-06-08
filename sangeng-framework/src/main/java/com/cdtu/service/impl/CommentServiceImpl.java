package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.Comment;
import com.cdtu.domain.vo.CommentVo;
import com.cdtu.domain.vo.PageVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.mapper.CommentMapper;
import com.cdtu.service.CommentService;
import com.cdtu.service.UserService;
import com.cdtu.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author mika
 * @since 2023-05-14 18:19:19
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        // 查询当前文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        // 时间升序
        queryWrapper.orderByDesc(Comment::getCreateTime);
        // articleId
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);
        // 根评论 -1
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_IS_ROOT);

        // 评论类型
        queryWrapper.eq(Comment::getType, commentType);

        // 分页查询
        Page<Comment> curPage = new Page<>(pageNum, pageSize);
        page(curPage, queryWrapper);

        List<CommentVo> commentVos = toCommentVoList(curPage.getRecords());

        // 查询根评论的子评论集合
        commentVos = commentVos.stream()
                .peek(commentVo -> {
                    // 查询子评论
                    List<CommentVo> children = getChildren(commentVo.getId());
                    // 赋值
                    commentVo.setChildren(children);
                })
                .collect(Collectors.toList());

        return ResponseResult.okResult(new PageVo<>(commentVos, curPage.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        // 评论不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * Comment 转换成 CommentVo 列表
     * @param comments Comment 列表
     * @return CommentVo 列表
     */
    private List<CommentVo> toCommentVoList(List<Comment> comments) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(comments, CommentVo.class);
        // 补充缺少的字段值：username 和 toCommentUsername
        commentVos = commentVos.stream()
                .peek(commentVo -> {
                    // toCommentUsername
                    Long toCommentUserId = commentVo.getToCommentUserId();
                    if (toCommentUserId != SystemConstants.COMMENT_NO_OBJECT) {
                        String toCommentUsername = userService.getById(toCommentUserId).getNickName();
                        commentVo.setToCommentUserName(toCommentUsername);
                    }
                    // username
                    Long usernameId = commentVo.getCreateBy();
                    String username = userService.getById(usernameId).getNickName();
                    commentVo.setUsername(username);
                })
                .collect(Collectors.toList());
        return commentVos;
    }

    /**
     * 查询子评论
     * @param id 评论 id
     * @return 子评论
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        // 查询当前 id 对应的 toCommentId
        queryWrapper.eq(Comment::getToCommentId, id);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        // 赋值
        List<CommentVo> children = toCommentVoList(list(queryWrapper));
        return children;
    }
}
