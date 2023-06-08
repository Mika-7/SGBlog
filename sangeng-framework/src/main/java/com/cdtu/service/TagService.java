package com.cdtu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.ModifyTagDto;
import com.cdtu.domain.dto.TagDto;
import com.cdtu.domain.dto.TagListDto;
import com.cdtu.domain.entity.Tag;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author mika
 * @since 2023-05-17 23:15:04
 */
public interface TagService extends IService<Tag> {

    ResponseResult<?> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult<?> addTag(TagDto tagDto);

    ResponseResult<?> removeTag(Long id);

    ResponseResult<?> getTag(Long id);

    ResponseResult<?> modifyTag(ModifyTagDto modifyTagDto);

    ResponseResult<?> listAllTag();
}
