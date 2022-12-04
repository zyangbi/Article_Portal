package com.imooc.api.config;

import com.imooc.api.interceptor.*;
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
    private UserActiveInterceptor userActiveInterceptor;
    @Autowired
    private AdminTokenInterceptor adminTokenInterceptor;
    @Autowired
    private ArticleReadInterceptor articleReadInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor)
                .addPathPatterns("/passport/getSMSCode");

        registry.addInterceptor(userTokenInterceptor)
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo")
                .addPathPatterns("/fans/follow")
                .addPathPatterns("/fans/unfollow");

        registry.addInterceptor(userActiveInterceptor)
                .addPathPatterns("/fans/follow")
                .addPathPatterns("/fans/unfollow");

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

        registry.addInterceptor(articleReadInterceptor)
                .addPathPatterns("/portal/article/readArticle");

    }

}
