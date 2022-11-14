package com.imooc.api.controller.user;

import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;


@RequestMapping("appUser")
@Api(value = "UserMngControllerApi")
public interface UserMngControllerApi {

    @PostMapping("queryAll")
    @ApiOperation(value = "get app user list", notes = "get app user list", httpMethod = "POST")
    public GraceJSONResult getAppUserList(@RequestParam String nickname,
                                          @RequestParam Integer status,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                          @RequestParam Integer page,
                                          @RequestParam Integer pageSize);

    @PostMapping("userDetail")
    @ApiOperation(value = "get user detail", notes = "get user detail", httpMethod = "POST")
    public GraceJSONResult getUser(@RequestParam String userId);

    @PostMapping("freezeUserOrNot")
    @ApiOperation(value = "update user status", notes = "update user status", httpMethod = "POST")
    public GraceJSONResult updateUserStatus(@RequestParam String userId,
                                            @RequestParam(name = "doStatus") Integer status);

}
