package com.sm.cn.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if (user == null){
            response.setContentType("application/json;charset=utf-8");
            String str = "{\"status\":60000,\"msg\":\"未登录\"}";
            response.getWriter().write(str);
            return false;
        }
        return true;
    }
}
