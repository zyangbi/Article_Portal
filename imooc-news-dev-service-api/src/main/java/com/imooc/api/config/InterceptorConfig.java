package com.imooc.api.config;

import com.imooc.api.interceptor.PassportInterceptor;
import com.imooc.api.interceptor.UserActiveInterceptor;
import com.imooc.api.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private UserActiveInterceptor userActiveInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor)
                .addPathPatterns("/passport/getSMSCode");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo");
//        registry.addInterceptor(userActiveInterceptor);
    }

}
