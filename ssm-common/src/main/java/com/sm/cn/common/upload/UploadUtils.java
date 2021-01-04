package com.sm.cn.common.upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.InputStream;
import java.util.ResourceBundle;

public class UploadUtils {

    private static String aliyun_key ;
    private static String aliyun_secret ;
    private static String aliyun_endpoint;
    private static String aliyun_bucketName;
    private static String aliyun_baseUrl ;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("aliyun");
        aliyun_key = rb.getString("aliyun_key");
        aliyun_secret = rb.getString("aliyun_secret");
        aliyun_endpoint = rb.getString("aliyun_endpoint");
        aliyun_bucketName = rb.getString("aliyun_bucketName");
        aliyun_baseUrl = rb.getString("aliyun_baseUrl");
    }

    public static String upload(InputStream is,String fileName){

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
