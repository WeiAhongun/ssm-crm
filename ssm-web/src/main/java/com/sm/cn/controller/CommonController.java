package com.sm.cn.controller;

import com.sm.cn.common.http.AxiosResult;
import com.sm.cn.common.http.AxiosStatus;
import com.sm.cn.entity.Employee;
import com.sm.cn.service.AsyncService;
import com.sm.cn.service.EmployeeService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class CommonController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FreeMarkerConfigurer markerConfigurer;

    @Autowired
    private AsyncService asyncService;

    @GetMapping("getAddress")
    public AxiosResult getAddress(@RequestParam String fid) throws IOException {
        /*URL url = new URL("http://apis.juhe.cn/xzqh/query?key=0edcaf1016494deb682b5b8a5239be8e&fid="+fid);
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);

        return AxiosResult.success(new String(bytes,"utf-8"));*/
        String forObject = restTemplate.getForObject("http://apis.juhe.cn/xzqh/query?key=0edcaf1016494deb682b5b8a5239be8e&fid=" + fid, String.class);
        return AxiosResult.success(forObject);
    }

    @GetMapping("sendSimpleEmail")
    public AxiosResult sendSimpleEmail(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("阿里嘎巴码云<a1293233730@163.com>");
        simpleMailMessage.setTo("1293233730@qq.com");
        simpleMailMessage.setSubject("入职通知书");
        simpleMailMessage.setText("月薪12000，一年14薪，请于本月底来公司报道");
        javaMailSender.send(simpleMailMessage);
        return AxiosResult.success();
    }

    @PostMapping("sendEmailWithFile")
    public AxiosResult sendEmailWithFile(String to, String subject, String text, @RequestPart Part file) throws Exception{

        String cd = file.getHeader("Content-Disposition");
        String filename = cd.substring(cd.lastIndexOf("=")+2, cd.length()-1);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //使用mimeMessageHelper给mimeMessage封装值
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom("阿里嘎巴码云<a1293233730@163.com>");
        mimeMessageHelper.setTo("1293233730@qq.com");
        mimeMessageHelper.setSubject("入职通知书");
        mimeMessageHelper.setText("月薪12000，一年14薪，请于本月底来公司报道");
        //传附件,把io流变成spring的流
        InputStream inputStream = file.getInputStream();
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);
        //传附件，需要spring流和文件名称
        mimeMessageHelper.addAttachment(filename,byteArrayResource);
        javaMailSender.send(mimeMessage);


        return AxiosResult.success();
    }


    @GetMapping("sendEmailWithFile")
    public AxiosResult sendEmailWithHtml() throws Exception{

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //使用mimeMessageHelper给mimeMessage封装值
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"utf-8");
        mimeMessageHelper.setFrom("阿里嘎巴码云<a1293233730@163.com>");
        mimeMessageHelper.setTo("1293233730@qq.com");
        mimeMessageHelper.setSubject("入职通知书");
        mimeMessageHelper.setText("<h1>月薪12000，一年14薪，请于本月底来公司报道<h1><img src='cid:imageUrl'>",true);

        mimeMessageHelper.addInline("imageUrl",new File("C:\\Users\\JAVASM\\Desktop\\img\\scenery01.jpg"));
        javaMailSender.send(mimeMessage);

        return AxiosResult.success();
    }

    @GetMapping("getEmailCode")
    public AxiosResult getEmailCode(String email){
        Employee employee = employeeService.getEmployeeByEmail(email);

        if(employee == null){
            return AxiosResult.custom(AxiosStatus.EMPLOYEE_NOT_FOUND);
        }else {
            if(employee.getActive()){
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom("阿里嘎巴码云<a1293233730@163.com>");
                simpleMailMessage.setTo(email);
                simpleMailMessage.setSubject("登入验证码");

                //获得100000到999999的随机数
                int sixNum = (int) (Math.random()*(999999-100000+1)+100000);

                simpleMailMessage.setText("您的验证码(2分钟失效):"+String.valueOf(sixNum));
                javaMailSender.send(simpleMailMessage);
                //往redis设值，2分钟失效
                stringRedisTemplate.opsForValue().set(email,String.valueOf(sixNum),2, TimeUnit.MINUTES);
                return AxiosResult.success();
            }else {
                return AxiosResult.custom(AxiosStatus.EMPLOYEE_NOT_ACTIVE);
            }
        }

    }

    @GetMapping("doLogin")
    public AxiosResult doLogin(String email, String checkCode, HttpSession session){
        String s = stringRedisTemplate.opsForValue().get(email);
        if(checkCode.equalsIgnoreCase(s)){
            session.setAttribute("user","user");
            stringRedisTemplate.delete(email);
            return AxiosResult.success();
        }
        return AxiosResult.error();
    }

    @GetMapping("getActiveLink")
    public AxiosResult getActiveLink(String email) throws MessagingException, IOException, TemplateException {
        Employee employee = employeeService.getEmployeeByEmail(email);
        if(employee == null){
            return AxiosResult.custom(AxiosStatus.EMPLOYEE_NOT_FOUND);
        }
        if(employee.getActive()){
            return AxiosResult.custom(AxiosStatus.EMPLOYEE_ACTIVEING);
        }else {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
            mimeMessageHelper.setFrom("阿里嘎巴码云<a1293233730@163.com>");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("激活验证");
            //往Redis中存uuid-email
            String uuid = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set(uuid,email,5,TimeUnit.MINUTES);


            //获得配置
            Configuration configuration = markerConfigurer.getConfiguration();
            //获得.ftl模板
            Template template = configuration.getTemplate("aliyun.ftl", "utf-8");
            //准备数据
            HashMap<String, String> map = new HashMap<>();
            map.put("employeeName",employee.getEmployeeName());
            map.put("activeLink","http://localhost:8080/employee/doActive?uuid="+uuid);
            //数据和模板整合，写到内存中
            StringWriter stringWriter = new StringWriter();
            template.process(map,stringWriter);
            stringWriter.flush();
            String s = stringWriter.toString();

            mimeMessageHelper.setText(s,true);
            //javaMailSender.send(mimeMessage);
            System.out.println(Thread.currentThread().getName());
            asyncService.senderEmail(mimeMessage);
            return AxiosResult.custom(AxiosStatus.EMAIL_SENDER_SUCCESS);
        }
    }

    @GetMapping("employee/doActive")
    public void employeeDoActive(String uuid, HttpServletResponse response) throws IOException {
        String email = stringRedisTemplate.opsForValue().get(uuid);
        if(StringUtils.isEmpty(email)){
            response.sendRedirect("http://localhost:63342/webapp/login1.html?_ijt=72tnn96nbi0j5l4694nvsdht8i&msg=unusable");
        }else {
            Employee employee = employeeService.getEmployeeByEmail(email);
            employee.setActive(true);
            employeeService.updateEmployee(employee);
            //链接点击一次后就删除
            stringRedisTemplate.delete(uuid);
            //用户点击连接后跳转到登入页面(开发环境下失效)
            response.sendRedirect("http://localhost:63342/webapp/login1.html?_ijt=72tnn96nbi0j5l4694nvsdht8i&msg=ok");
        }
    }


}
