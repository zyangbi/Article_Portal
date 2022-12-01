package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.exception.GraceException;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AccountInfoVO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.UserInfoVO;
import com.imooc.user.service.UserService;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController extends BaseController implements UserControllerApi {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redis;

    @Override
    public GraceJSONResult getAccountInfo(String userId) {
        AppUser user = getUser(userId);
        AccountInfoVO accountInfo = new AccountInfoVO();
        BeanUtils.copyProperties(user, accountInfo);
        return GraceJSONResult.ok(accountInfo);
    }

    @Override
    public GraceJSONResult getUserInfo(String userId) {
        AppUser user = getUser(userId);
        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfo);
        return GraceJSONResult.ok(userInfo);
    }

    @Override
    public GraceJSONResult updateUserInfo(UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
        if (result.hasErrors()) {
            return GraceJSONResult.errorMap(getErrorMap(result));
        }

        // first delete
        String userId = updateUserInfoBO.getId();
        redis.del(REDIS_USER_INFO + userId);

        // update user info in database
        userService.updateUser(updateUserInfoBO);
        try {
            Thread.sleep(1000);
            // second delete
            redis.del(REDIS_USER_INFO + userId);
            AppUser user = userService.getUser(userId);
            redis.set(REDIS_USER_INFO + userId, JsonUtils.objectToJson(user), MONTH);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getUserVOs(String userIds) {
        List<AppUserVO> userVOList = new ArrayList<>();
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
        for (String userId : userIdList) {
            AppUserVO userVO = getUserVO(userId);
            userVOList.add(userVO);
        }
        return GraceJSONResult.ok(userVOList);
    }

    private AppUser getUser(String userId) {
        AppUser user;
        String userJson = redis.get(REDIS_USER_INFO + userId);
        if (StringUtils.isNotBlank(userJson)) {
            // user in redis
            user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        } else {
            user = userService.getUser(userId);
            if (user != null) {
                // user not in redis
                redis.set(REDIS_USER_INFO + userId, JsonUtils.objectToJson(user), MONTH);
            } else {
                // user doesn't exit
                GraceException.display(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
            }
        }
        return user;
    }

    private AppUserVO getUserVO(String userId) {
        AppUser user = getUser(userId);
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
