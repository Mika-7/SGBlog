package com.cdtu.controller;

import com.cdtu.domain.ResponseResult;
import com.cdtu.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {
    @Resource
    UploadService uploadService;
    @PostMapping("/upload")
    public ResponseResult<?> uploadImg(@RequestParam("img") MultipartFile img) {
        try {
            return uploadService.uploadImg(img);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传图片失败");
        }
    }
}
