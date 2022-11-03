package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminBO;
import com.imooc.utils.PagedGridResult;

public interface AdminUserService {

    public AdminUser getAdminByUsername(String username);

    public void createAdmin(AdminBO adminBO);

    public PagedGridResult getAdminList(Integer page, Integer pageSize);

}
