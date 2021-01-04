package com.sm.cn.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sm.cn.common.http.AxiosResult;
import com.sm.cn.common.upload.UploadService;
import com.sm.cn.common.upload.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

@RestController

public class UploadController {

    @Autowired
    private UploadService uploadService;
    @PostMapping("upload")
    public AxiosResult upload(HttpServletRequest request) throws IOException, ServletException {

        Part part = request.getPart("avatar");

        String cd = part.getHeader("Content-Disposition");
        String filename = cd.substring(cd.lastIndexOf("=")+2, cd.length()-1);
        String s =UUID.randomUUID().toString()+"."+ filename;

        InputStream inputStream = part.getInputStream();
        String url = uploadService.upload(inputStream,s);
        return AxiosResult.success(url);





        /*Part part = request.getPart("avatar");

        String cd = part.getHeader("Content-Disposition");
        String filename = cd.substring(cd.lastIndexOf("=")+2, cd.length()-1);
        String s =UUID.randomUUID().toString()+"."+ filename;

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4Fz1MSD7vVygd6ii1rer";
        String accessKeySecret = "kBNRMvxesvqYwagYJGbyuicoWO7Kq9";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传网络流。
        InputStream inputStream = part.getInputStream();
        ossClient.putObject("shangmaershiwuqi", s, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        String url = "https://shangmaershiwuqi.oss-cn-beijing.aliyuncs.com/"+s;
        return AxiosResult.success(url);*/





        /*Part part = request.getPart("avatar");
        String realPath = request.getServletContext().getRealPath("/upload/");

        File file = new File(realPath);
        if(!file.exists())
        {
            file.mkdirs();
        }

        //获得后缀
        String cd = part.getHeader("Content-Disposition");
        String filename = cd.substring(cd.lastIndexOf("=")+2, cd.length()-1);
        String s =UUID.randomUUID().toString()+"."+ filename;
        part.write(realPath+s);

        return AxiosResult.success("http://localhost:8080/upload/"+s);*/

    }

    @PostMapping("uploadByStr")
    public AxiosResult uploadByStr(@RequestBody Map<String,Object> map) {
        System.out.println("***************"+map);
        return AxiosResult.success();
    }
}
