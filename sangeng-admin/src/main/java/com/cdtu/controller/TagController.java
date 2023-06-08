package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.ModifyTagDto;
import com.cdtu.domain.dto.TagDto;
import com.cdtu.domain.dto.TagListDto;
import com.cdtu.domain.entity.Tag;
import com.cdtu.domain.vo.TagVo;
import com.cdtu.service.TagService;
import com.cdtu.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Resource
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult<?> list(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize,
            TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }
    @GetMapping("/listAllTag")
    public ResponseResult<?> listAllTag() {
        return tagService.listAllTag();
    }
    @PostMapping
    public ResponseResult<?> addTag(@RequestBody TagDto tagDto) {
        return tagService.addTag(tagDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult<?> removeTag(@PathVariable("ids") String ids) {
        String[] idArray = ids.split(",");
        for (String str_id : idArray) {
            Long id = Long.parseLong(str_id);
            tagService.removeTag(id);
        }
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult<?> getTag(@PathVariable("id")Long id) {
        return tagService.getTag(id);
    }
    @PutMapping
    public ResponseResult<?> modifyTag(@RequestBody ModifyTagDto modifyTagDto) {
        return tagService.modifyTag(modifyTagDto);
    }
}
