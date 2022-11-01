package com.imooc.api.interceptor;

import com.imooc.enums.ResponseStatusEnum;
import com.imooc.exception.GraceException;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseInterceptor {
    public static final String REDIS_TOKEN = "token:";
    public static final String REDIS_USER = "user:";

    @Autowired
    private RedisOperator redis;

    public boolean verifyUserIdToken(String id, String token, String redis_prefix) {
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(token)) {
            String redis_token = redis.get(redis_prefix + id);
            if (StringUtils.isNotBlank(redis_token)) {
                if (!StringUtils.equalsIgnoreCase(token, redis_token)) {
                    // token error
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            } else {
                // token not in redis
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            }
        } else {
            // id or token empty
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        return true;
    }

}
