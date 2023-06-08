package com.cdtu.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebUtils {
    /**
     * 渲染 string 响应
     * @param response
     * @param string
     */
    public static void renderString(HttpServletResponse response, String string) {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().print(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//    public static void setDownloadHeader(String filename, ServletContext context, HttpServletResponse response) throws UnsupportedEncodingException {
//        String mimeType = context.getMimeType(filename);
//        response.setHeader("content-type", mimeType);
//        String fname = URLEncoder.encode(filename,"UTF-8");
//        response.setHeader("Content-disposition", "attachment; filename=" + fname);
//    }

    /**
     * 设置下载 excel xlsx 文件的响应头
     * @param fileName 文件名
     * @param response 响应
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    public static void setExcelHeader(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fname= URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition","attachment; filename="+fname);
    }
}
