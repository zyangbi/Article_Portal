package com.imooc.api;

import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public static final String MOBILE_IP = "ip:sms:";
    public static final String REDIS_USER_INFO = "info:user:";
    public static final String REDIS_ADMIN_TOKEN = "token:admin:";
    public static final String REDIS_USER_TOKEN = "token:user:";
    public static final String REDIS_CATEGORY = "category:";
    public static final String REDIS_FANS_COUNT = "count:fans:";
    public static final String REDIS_FOLLOW_COUNT = "count:follow:";
    public static final String REDIS_READ_COUNT = "count:read:";
    public static final String REDIS_READ_IP = "ip:read:";
    public static final Integer PAGE_DEFAULT = 0;
    public static final Integer PAGE_SIZE_DEFAULT = 10;
    public static final Integer MONTH = 30 * 24 * 60 * 60;
    public static final String[] provinceList = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海",
            "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};

    @Value("${website.domain}")
    public String DOMAIN_NAME;

    @Autowired
    private RedisOperator redis;

    public Integer getCountFromRedis(String key) {
        String countStr = redis.get(key);
        if (StringUtils.isBlank(countStr)) {
            countStr = "0";
        }
        return Integer.valueOf(countStr);
    }

    public Integer setPage(Integer page) {
        if (page == null) {
            page = PAGE_DEFAULT;
        }
        return page;
    }

    public Integer setPageSize(Integer pageSize) {
        if (pageSize == null) {
            pageSize = PAGE_SIZE_DEFAULT;
        }
        return pageSize;
    }

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
