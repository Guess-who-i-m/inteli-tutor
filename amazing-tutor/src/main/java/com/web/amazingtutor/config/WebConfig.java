package com.web.amazingtutor.config;

import com.web.amazingtutor.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录接口和注册接口不拦截
         registry.addInterceptor(loginInterceptor).excludePathPatterns("/api/student/login", "/api/student/register",  "/api/teacher/register", "/api/teacher/login");
    }
}