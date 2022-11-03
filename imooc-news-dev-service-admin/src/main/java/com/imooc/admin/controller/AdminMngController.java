package com.imooc.admin.controller;

import com.imooc.admin.service.AdminUserService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.AdminMngControllerApi;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.exception.GraceException;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminBO;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RedisOperator redis;

    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return GraceJSONResult.errorMap(getErrorMap(result));
        }

        AdminUser admin = adminUserService.getAdminByUsername(adminLoginBO.getUsername());
        if (admin == null) {
            // admin not exist error
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        if (!BCrypt.checkpw(adminLoginBO.getPassword(), admin.getPassword())) {
            // password error
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }

        // set redis and cookie
        String token = UUID.randomUUID().toString().trim();
        redis.set(REDIS_ADMIN_TOKEN + admin.getId(), token);
        setCookie("atoken", token, request, response, MONTH);
        setCookie("aid", admin.getId(), request, response, MONTH);
        setCookie("aname", admin.getAdminName(), request, response, MONTH);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminIsExist(String username) {
        checkAdminExist(username);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult addNewAdmin(AdminBO adminBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        // 1. image and password can not both be blank
        if (StringUtils.isBlank(adminBO.getImg64()) && StringUtils.isBlank(adminBO.getPassword())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        }
        // 2. check password
        if (StringUtils.isNotBlank(adminBO.getPassword())) {
            if (!StringUtils.equalsIgnoreCase(adminBO.getPassword(), adminBO.getConfirmPassword())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        // 3. check username not exist
        checkAdminExist(adminBO.getUsername());
        // 4. create new admin user
        adminUserService.createAdmin(adminBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {
        // 1. if param is null, set default value
        if (page == null) {
            page = PAGE_DEFAULT;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE_DEFAULT;
        }
        // 2. get admin list
        PagedGridResult result = adminUserService.getAdminList(page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {
        deleteCookie("atoken", request, response);
        deleteCookie("aid", request, response);
        deleteCookie("aname", request, response);
        redis.del(REDIS_ADMIN_TOKEN + adminId);
        return GraceJSONResult.ok();
    }

    private void checkAdminExist(String username) {
        AdminUser admin = adminUserService.getAdminByUsername(username);
        if (admin != null) {
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

}
