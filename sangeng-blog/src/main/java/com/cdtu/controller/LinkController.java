package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/link")
@Api(tags = "友链", description = "友链相关接口")
public class LinkController {
    @Resource
    LinkService linkService;
    @GetMapping("/getAllLink")
    @ApiOperation(value = "获取友链", notes = "获取所有的友链")
    public ResponseResult<?> getAllLink() {
        return linkService.getAllLink();
    }
}
