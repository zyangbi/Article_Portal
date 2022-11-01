package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.enums.UserStatus;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.RegisterLoginBO;
import com.imooc.user.service.UserService;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.IPUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    @Autowired
    private RedisOperator redis;

    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {

        // Set 60 seconds timer for this IP address
        String userIP = IPUtil.getRequestIp(request);
        redis.set(MOBILE_IP + userIP, userIP, 60);

        // Send SMS code
        String sms_code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        redis.set(MOBILE_SMSCODE + mobile, sms_code, 30 * 60);
        // smsUtils.sendSMS(mobile, random);

        return GraceJSONResult.ok(sms_code);
    }

    @Override
    public GraceJSONResult doLogin(RegisterLoginBO registerLoginBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        if (result.hasErrors()) {
            return GraceJSONResult.errorMap(getErrorMap(result));
        }

        // 1. verify SMS code
        String mobile = registerLoginBO.getMobile();
        String smsCode = registerLoginBO.getSmsCode();
        String redisSMS = redis.get(MOBILE_SMSCODE + mobile);
        if (StringUtils.isBlank(redisSMS) || !StringUtils.equalsIgnoreCase(smsCode, redisSMS)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 2. Register or Login
        AppUser user = userService.getUserByMobile(mobile);
        if (user == null) {
            // create new user if not exit
            user = userService.createUser(mobile);
        } else if (user.getActiveStatus() == UserStatus.FROZEN.type) {
            // return error if user is frozen
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        }

        // 3. Set Redis
        String token = UUID.randomUUID().toString().trim();
        String id = user.getId();
        redis.set(REDIS_TOKEN + id, token, MONTH);
        redis.set(REDIS_USER + id, JsonUtils.objectToJson(user), MONTH);

        // 4. Send cookie with token and id
        setCookie("utoken", token, request, response, MONTH);
        setCookie("uid", id, request, response, MONTH);

        // 5. Delete SMS in redis
        redis.del(MOBILE_SMSCODE + mobile);

        return GraceJSONResult.ok(user.getActiveStatus());
    }

    @Override
    public GraceJSONResult logout(String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        // delete cookie
        setCookie("utoken", "", request, response, MONTH);
        setCookie("uid", "", request, response, MONTH);

        // delete redis token
        redis.del(REDIS_TOKEN + userId);

        return GraceJSONResult.ok();
    }
}
