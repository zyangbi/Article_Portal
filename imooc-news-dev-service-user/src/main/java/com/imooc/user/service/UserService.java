package com.imooc.user.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.utils.PagedGridResult;

import java.util.Date;

public interface UserService {

    public AppUser getUserByMobile(String mobile);

    public AppUser createUser(String mobile);

    public AppUser getUser(String userId);

    public void updateUser(UpdateUserInfoBO updateUserInfoBO);

    public void updateUserStatus(String id, Integer status);

    public PagedGridResult getUserList(String nickname, Integer status, Date startDate,
                                       Date endDate, Integer page, Integer pageSize);

}
