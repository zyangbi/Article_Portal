package com.imooc.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    public static final String MOBILE_SMSCODE = "sms:mobile:";
    public static final String MOBILE_IP = "ip:mobile:";
    public static final String REDIS_USER_INFO = "info:user:";
    public static final String REDIS_ADMIN_TOKEN = "token:admin:";
    public static final String REDIS_USER_TOKEN = "token:user:";
    public static final String REDIS_CATEGORY = "category:";
    public static final Integer PAGE_DEFAULT = 0;
    public static final Integer PAGE_SIZE_DEFAULT = 10;
    public static final Integer MONTH = 30 * 24 * 60 * 60;
    public static final Integer DAY = 24 * 60 * 60;

    @Value("${website.domain}")
    public String DOMAIN_NAME;

    public Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            map.put(error.getField(), error.getDefaultMessage());
        }
        return map;
    }

    public void setCookie(String cookieName,
                          String cookieValue,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          int maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            Cookie cookie = new Cookie(cookieName, cookieValue);
            cookie.setMaxAge(maxAge);
            cookie.setDomain(DOMAIN_NAME);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteCookie(String cookieName,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        try {
            String cookieValue = URLEncoder.encode("", "utf-8");
            setCookie(cookieName, cookieValue, request, response, 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
