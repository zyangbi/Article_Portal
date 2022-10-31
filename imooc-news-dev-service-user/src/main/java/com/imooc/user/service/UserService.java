package com.imooc.user.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;

public interface UserService {

    public AppUser getUserByMobile(String mobile);

    public AppUser createUser(String mobile);

    public AppUser getUser(String userId);

    public void updateUser(UpdateUserInfoBO updateUserInfoBO);

}
