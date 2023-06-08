package com.cdtu.service;

import com.cdtu.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
    ResponseResult<?> uploadImg(MultipartFile img);
}
