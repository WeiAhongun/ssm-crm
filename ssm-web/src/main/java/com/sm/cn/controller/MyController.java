package com.sm.cn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("t1")
    public String f1(){
        return "Hello Spring MVC!";
    }
}
