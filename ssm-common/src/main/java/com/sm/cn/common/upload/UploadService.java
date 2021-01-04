package com.sm.cn.common.upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@PropertySource("classpath:aliyun.properties")
public class UploadService {
    @Value("${aliyun_key}")
    private  String aliyun_key ;
    @Value("${aliyun_secret}")
    private  String aliyun_secret ;
    @Value("${aliyun_endpoint}")
    private  String aliyun_endpoint;
    @Value("${aliyun_bucketName}")
    private  String aliyun_bucketName;
    @Value("${aliyun_baseUrl}")
    private  String aliyun_baseUrl ;

    public String upload(InputStream is, String fileName){

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(aliyun_endpoint, aliyun_key, aliyun_secret);

        // 上传网络流。
        ossClient.putObject(aliyun_bucketName, fileName, is);

        // 关闭OSSClient。
        ossClient.shutdown();

        String url = aliyun_baseUrl+fileName;
        return url;
    }
}
