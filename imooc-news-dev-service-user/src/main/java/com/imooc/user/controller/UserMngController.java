package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.UserMngControllerApi;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.enums.UserStatus;
import com.imooc.pojo.AppUser;
import com.imooc.user.service.UserService;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UserMngController extends BaseController implements UserMngControllerApi {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redis;

    @Override
    public GraceJSONResult getAppUserList(String nickname, Integer status, Date startDate,
                                          Date endDate, Integer page, Integer pageSize) {
        if (page == null) {
            page = PAGE_DEFAULT;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE_DEFAULT;
        }

        PagedGridResult result = userService.getUserList(nickname, status, startDate, endDate, page, pageSize);

        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult getUser(String userId) {
        AppUser user = userService.getUser(userId);
        return GraceJSONResult.ok(user);
    }

    @Override
    public GraceJSONResult updateUserStatus(String userId, Integer status) {
        if (!UserStatus.isUserStatusValid(status)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        userService.updateUserStatus(userId, status);
        redis.del(REDIS_USER_INFO + userId);
        return GraceJSONResult.ok();
    }
}
