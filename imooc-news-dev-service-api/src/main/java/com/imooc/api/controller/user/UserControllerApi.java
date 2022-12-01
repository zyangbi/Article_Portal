package com.imooc.api.controller.user;

import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping("/user")
@Api(value = "UserControllerApi")
public interface UserControllerApi {

    @PostMapping("/getAccountInfo")
    @ApiOperation(value = "get account info (more detail)", notes = "get account info", httpMethod = "POST")
    public GraceJSONResult getAccountInfo(@RequestParam @NotBlank String userId);

    @PostMapping("/getUserInfo")
    @ApiOperation(value = "get user info", notes = "get user info", httpMethod = "POST")
    public GraceJSONResult getUserInfo(@RequestParam @NotBlank String userId);

    @PostMapping("/updateUserInfo")
    @ApiOperation(value = "update user info", notes = "update user info", httpMethod = "POST")
    public GraceJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO,
                                          BindingResult result);

    @GetMapping("/queryByIds")
    @ApiOperation(value = "query user by id", notes = "query user by id", httpMethod = "GET")
    public GraceJSONResult getUserVOs(@RequestParam @NotBlank String userIds);

}
