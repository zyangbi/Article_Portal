package com.imooc.api.config;

import com.imooc.api.interceptor.AdminTokenInterceptor;
import com.imooc.api.interceptor.PassportInterceptor;
import com.imooc.api.interceptor.UserTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private PassportInterceptor passportInterceptor;
    @Autowired
    private UserTokenInterceptor userTokenInterceptor;
    @Autowired
    private AdminTokenInterceptor adminTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor)
                .addPathPatterns("/passport/getSMSCode");

        registry.addInterceptor(userTokenInterceptor)
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo");

        registry.addInterceptor(adminTokenInterceptor)
                .addPathPatterns("/adminMng/adminIsExist")
                .addPathPatterns("/adminMng/addNewAdmin")
                .addPathPatterns("/adminMng/getAdminList")
                .addPathPatterns("/adminMng/adminLogout")
                .addPathPatterns("/friendLinkMng/saveOrUpdateFriendLink")
                .addPathPatterns("/friendLinkMng/getFriendLinkList")
                .addPathPatterns("/friendLinkMng/delete")
                .addPathPatterns("/appUser/queryAll")
                .addPathPatterns("/appUser/userDetail")
                .addPathPatterns("/appUser/freezeUserOrNot")
                .addPathPatterns("/categoryMng/saveOrUpdateCategory")
                .addPathPatterns("/categoryMng/getCatList");


    }

}
