package com.cdtu.service.impl;

import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.User;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.service.UploadService;
import com.cdtu.service.UserService;
import com.cdtu.utils.PathUtils;
import com.cdtu.utils.SecurityUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ConfigurationProperties(prefix = "oss")
@Data
@Service("uploadService")
public class UploadServiceImpl implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String url;
    @Resource
    private UserService userService;
    @Override
    public ResponseResult<?> uploadImg(MultipartFile img) {
        // TODO：判断文件大小或者文件类型
        String fileName = img.getOriginalFilename();
        Pattern pattern = Pattern.compile(SystemConstants.IMAGE_SUFFIX);
        Matcher matcher = pattern.matcher(Objects.requireNonNull(fileName));

        if (!matcher.find()) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        fileName = PathUtils.generateFilePath(fileName);
        // 上传到 OSS
        String url = uploadOSS(img, fileName);

        return ResponseResult.okResult(url);
    }
    private String uploadOSS(MultipartFile img, String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        // 指定分片上传版本
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
//        String accessKey = "";
//        String secretKey = "";
//        String bucket = "mika-sg-blog";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;

        try {
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                com.qiniu.http.Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                // key 为文件路径
                System.out.println(putRet.key);
                // 生成的 Hash 值
                System.out.println(putRet.hash);
            }
            catch (QiniuException ex) {
                com.qiniu.http.Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                }
                catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return url + key;
    }
}
