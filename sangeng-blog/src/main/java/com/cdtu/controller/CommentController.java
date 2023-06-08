package com.cdtu.controller;

import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddCommentDto;
import com.cdtu.domain.entity.Comment;
import com.cdtu.service.CommentService;
import com.cdtu.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {
    @Resource
    CommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation(value = "评论", notes = "获取一页评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章 ID"),
            @ApiImplicitParam(name = "pageNum", value = "第几页"),
            @ApiImplicitParam(name = "pageSize", value = "一页数量")
    })
    public ResponseResult commentList(
            @RequestParam("articleId") Long articleId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }
    @PostMapping
    @ApiOperation(value = "添加评论", notes = "添加用户的评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addCommentDto", value = "添加评论对象"),
    })
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }
    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论", notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页"),
            @ApiImplicitParam(name = "pageSize", value = "一页数量")
    })
    public ResponseResult linkComment(Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize);
    }
}
