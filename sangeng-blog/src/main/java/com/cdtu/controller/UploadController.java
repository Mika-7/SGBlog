package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@Api(tags = "上传", description = "上传文件相关接口")
public class UploadController {
    @Resource
    private UploadService uploadService;
    @PostMapping("/upload")
    @ApiOperation(value = "上传图片", notes = "通过服务器，将用户的头像上传到七牛云 OSS")
    @ApiImplicitParam(name = "img", value = "待上传的图片")
    public ResponseResult<?> uploadImg(MultipartFile img) {
        return uploadService.uploadImg(img);
    }
}
