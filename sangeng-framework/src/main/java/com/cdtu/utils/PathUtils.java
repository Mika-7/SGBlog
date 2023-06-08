package com.cdtu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 路径工具类
 */
public class PathUtils {
    public static String generateFilePath(String fileName) {
        // 根据日期，生成形如：2023/05/16 的格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String datePath = simpleDateFormat.format(new Date());
        // uuid 作为文件名
         String uuid = UUID.randomUUID().toString().replaceAll("-", "");
         // 获取文件后缀拓展名
        int index = fileName.indexOf(".");
        // xx.png => .png
        String fileType = fileName.substring(index);
        return datePath + uuid + fileType;
    }
}
