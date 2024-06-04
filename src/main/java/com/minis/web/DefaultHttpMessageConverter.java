package com.minis.web;

import com.minis.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

// 将Object转成JSON串
@Slf4j
public class DefaultHttpMessageConverter implements HttpMessageConverter {
    private final static String defaultContentType = "text/json;charset=UTF-8";
    private final static String defaultCharacterEncoding = "UTF-8";
    @Autowired
    ObjectMapper objectMapper;

    public void write(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType(defaultContentType);
        response.setCharacterEncoding(defaultCharacterEncoding);
        writeInternal(obj, response);
        response.flushBuffer();
    }
    // 给response写字符串
    private void writeInternal(Object obj, HttpServletResponse response) throws IOException{
        String sJsonStr = this.objectMapper.writeValuesAsString(obj);
        PrintWriter pw = response.getWriter();
        pw.write(sJsonStr);
    }
}