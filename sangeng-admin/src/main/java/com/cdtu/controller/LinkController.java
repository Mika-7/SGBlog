package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddLinkDto;
import com.cdtu.domain.dto.ListLinkDto;
import com.cdtu.domain.dto.ModifyLinkDto;
import com.cdtu.service.LinkService;
import org.ehcache.core.spi.store.Store;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    LinkService linkService;
    @GetMapping("/list")
    public ResponseResult<?> listLink(
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam("pageSize")Integer pageSize,
            ListLinkDto listLinkDto) {
        return linkService.listLink(pageNum, pageSize, listLinkDto);
    }
    @PostMapping
    public ResponseResult<?> addLink(@RequestBody AddLinkDto addLinkDto) {
        return linkService.addLink(addLinkDto);
    }
    @GetMapping("/{id}")
    public ResponseResult<?> getLinkById(@PathVariable("id")Long id) {
        return linkService.getLinkById(id);
    }
    @PutMapping
    public ResponseResult<?> modifyLink(@RequestBody ModifyLinkDto modifyLinkDto) {
        return linkService.modifyLink(modifyLinkDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult<?> removeLink(@PathVariable("ids")String ids) {
        String[] idArray = ids.split(",");
        for (String str_id : idArray) {
            Long id = Long.parseLong(str_id);
            linkService.removeLink(id);
        }
        return ResponseResult.okResult();
    }
}
