package com.sm.cn.controller;

import com.sm.cn.common.http.AxiosResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

@RestController
public class FreeMarkerController {

    @Autowired
    private FreeMarkerConfigurer markerConfigurer;

    @GetMapping("freemarker")
    public AxiosResult freemarker() throws IOException, TemplateException {
        //获得配置
        Configuration configuration = markerConfigurer.getConfiguration();
        //获得.ftl模板
        Template template = configuration.getTemplate("aliyun.ftl", "utf-8");
        //准备数据
        HashMap<String, String> map = new HashMap<>();
        map.put("employeeName","韦钟钧");
        map.put("activeLink","http://www.baidu.com");
        //数据和模板整合
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream("D://wzx.html"), "utf-8");
        template.process(map,outputStreamWriter);
        outputStreamWriter.close();
        return AxiosResult.success();
    }
}
