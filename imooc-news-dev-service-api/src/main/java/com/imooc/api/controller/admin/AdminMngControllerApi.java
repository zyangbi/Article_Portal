package com.imooc.api.controller.admin;

import com.imooc.pojo.bo.AdminBO;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping("/adminMng")
@Api(value = "AdminMngControllerApi")
public interface AdminMngControllerApi {

    @PostMapping("/adminLogin")
    @ApiOperation(value = "admin login", notes = "admin login", httpMethod = "POST")
    public GraceJSONResult adminLogin(@RequestBody @Valid AdminLoginBO adminLoginBO,
                                      BindingResult result,
                                      HttpServletRequest request,
                                      HttpServletResponse response);

    @PostMapping("/adminIsExist")
    @ApiOperation(value = "check admin existence", notes = "check admin existence", httpMethod = "POST")
    public GraceJSONResult adminIsExist(@RequestParam @NotBlank String username);

    @PostMapping("/addNewAdmin")
    @ApiOperation(value = "add new admin", notes = "add new admin", httpMethod = "POST")
    public GraceJSONResult addNewAdmin(@RequestBody @Valid AdminBO adminBO,
                                       BindingResult result,
                                       HttpServletRequest request,
                                       HttpServletResponse response);

    @PostMapping("/getAdminList")
    @ApiOperation(value = "get admin list", notes = "get admin list", httpMethod = "POST")
    public GraceJSONResult getAdminList(@ApiParam(name = "pageNum", value = "page number", required = false)
                                        @RequestParam @Nullable Integer pageNum,
                                        @ApiParam(name = "pageSize", value = "page size", required = false)
                                        @RequestParam @Nullable Integer pageSize);


    @PostMapping("/adminLogout")
    @ApiOperation(value = "admin logout", notes = "admin logout", httpMethod = "POST")
    public GraceJSONResult adminLogout(@RequestParam @NotBlank String adminId,
                                       HttpServletRequest request,
                                       HttpServletResponse response);
}
