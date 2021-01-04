package com.sm.cn.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Test {
    public static void main(String[] args) throws IOException {
        String run = run("http://apis.juhe.cn/xzqh/query?key=0edcaf1016494deb682b5b8a5239be8e&fid=0");
        System.out.println(run);


        /*String img = ImageToBase64("C:\\Users\\JAVASM\\Desktop\\idcard.jpg");
        HashMap<String, String> map = new HashMap<>();
        map.put("key","2debf5372e4e57de30bcbddec134373f");
        map.put("side","front");
        map.put("image",img);
        String post = post("http://apis.juhe.cn/idimage/verify", map);
        System.out.println(post);*/
    }

    public static String run(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private static String ImageToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(Objects.requireNonNull(data));
    }

    public static String post(String url, HashMap<String, String > paramsMap){  //这里没有返回，也可以返回string
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = paramsMap.keySet();
        for(String key:keySet) {
            String value = paramsMap.get(key);
            formBodyBuilder.add(key,value);
        }
        FormBody formBody = formBodyBuilder.build();
        Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .build();
        try (Response response = mOkHttpClient.newCall(request).execute()) {
           return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
