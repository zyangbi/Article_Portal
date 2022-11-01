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

    public static final String MOBILE_SMSCODE = "mobile:sms:";
    public static final String MOBILE_IP = "mobile:ip:";
    public static final String REDIS_USER = "user:";
    public static final String REDIS_TOKEN = "token:";

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

}
