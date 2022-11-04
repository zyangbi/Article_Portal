package com.imooc.api.controller.user;

import com.imooc.pojo.bo.RegisterLoginBO;
import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Api(value = "PassportControllerApi")
@RequestMapping("/passport")
public interface PassportControllerApi {

    @ApiOperation(value = "send SMS code", notes = "send SMS code", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam @NotBlank String mobile, HttpServletRequest request);

    @ApiOperation(value = "register & login", notes = "register & login", httpMethod = "POST")
    @PostMapping("/doLogin")
    public GraceJSONResult doLogin(@RequestBody @Valid RegisterLoginBO registerLoginBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response);

}
