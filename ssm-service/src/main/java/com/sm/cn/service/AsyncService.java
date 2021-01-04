package com.sm.cn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class AsyncService {

    @Autowired
    JavaMailSender javaMailSender;
    @Async
    public void senderEmail(MimeMessage mimeMessage){
        System.out.println(Thread.currentThread().getName());
        javaMailSender.send(mimeMessage);
    }
}
