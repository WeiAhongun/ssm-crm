package com.sm.cn.controller;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.sm.cn.common.http.AxiosResult;
import com.sm.cn.common.http.AxiosStatus;
import com.sm.cn.common.upload.UploadUtils;
import com.sm.cn.entity.Admin;
import com.sm.cn.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class AdminController {

    @Autowired
    private AdminService service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("admin")
    public List<Admin> admin(){
        System.out.println(666);
        return service.queryAllAdmin();
    }

    @GetMapping("getPhoneCheckcode/{phone}")
    public AxiosResult getPhoneCheckcode(@PathVariable String phone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4Fz1MSD7vVygd6ii1rer", "kBNRMvxesvqYwagYJGbyuicoWO7Kq9");
        IAcsClient client = new DefaultAcsClient(profile);

        int random = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "尚马十九期辉哥");
        request.putQueryParameter("TemplateCode", "SMS_183240934");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + random + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }

        redisTemplate.opsForValue().set(phone,random+"",5, TimeUnit.MINUTES);
        return AxiosResult.success();
    }

    @GetMapping("adminLogin/{code}/{phone}")
    public AxiosResult adminLogin(@PathVariable String code, @PathVariable String phone, HttpSession session){
        String redisCode = redisTemplate.opsForValue().get(phone);
        if(StringUtils.isEmpty(redisCode)){
            //返回50000，还没发验证码
            return AxiosResult.error();
        }
        if(code.equalsIgnoreCase(redisCode)){
            //返回20000，登入成功
            session.setAttribute("user","user");
            redisTemplate.delete(phone);
            return AxiosResult.success();
        }else {
            //40006,验证码错误
            return AxiosResult.success(AxiosStatus.Code_ERROR);
        }
    }
}
