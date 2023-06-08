package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.ModifyTagDto;
import com.cdtu.domain.dto.TagDto;
import com.cdtu.domain.dto.TagListDto;
import com.cdtu.domain.entity.Tag;
import com.cdtu.domain.vo.PageVo;
import com.cdtu.domain.vo.TagVo;
import com.cdtu.mapper.TagMapper;
import com.cdtu.service.TagService;
import com.cdtu.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 标签(Tag)表服务实现类
 *
 * @author mika
 * @since 2023-05-17 23:15:04
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    public ResponseResult<?> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // 分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        Page<Tag> curPage = new Page<>(pageNum, pageSize);
        page(curPage, queryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(curPage.getRecords(), TagVo.class);
        // 封装数据
        PageVo<TagVo> pageVo = new PageVo<>(tagVos, curPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> addTag(TagDto tagDto) {
        // dto => tag 实体对象
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        tag.setRemark(tagDto.getRemark());

        // 自动填充,保存到数据库中
        boolean isSave = save(tag);
        String result = isSave ? "保存成功" : "保存失败";
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult<?> removeTag(Long id) {
        boolean isDelete = removeById(id);
        String result = isDelete ? "保存成功" : "保存失败";
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult<?> getTag(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult<?> modifyTag(ModifyTagDto modifyTagDto) {
        Tag tag = BeanCopyUtils.copyBean(modifyTagDto, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getName, Tag::getId);
        List<Tag> tags = list(queryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tags, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}
