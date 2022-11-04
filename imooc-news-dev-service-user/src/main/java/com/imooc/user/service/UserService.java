package com.imooc.user.service;

import com.imooc.pojo.AppUser;

public interface UserService {

    public AppUser getUserByMobile(String mobile);

    public AppUser createUser(String mobile);

}
