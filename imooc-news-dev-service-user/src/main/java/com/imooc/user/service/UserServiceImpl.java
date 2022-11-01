package com.imooc.user.service;

import com.imooc.enums.ResponseStatusEnum;
import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.utils.DateUtil;
import com.imooc.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private Sid sid;

    @Override
    public AppUser getUserByMobile(String mobile) {
        Example example = new Example(AppUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile", mobile);
        AppUser user = appUserMapper.selectOneByExample(example);
        return user;
    }

    @Override
    public AppUser getUser(String userId) {
        AppUser user = appUserMapper.selectByPrimaryKey(userId);
        return user;
    }

    @Transactional
    @Override
    public AppUser createUser(String mobile) {
        AppUser user = new AppUser();

        user.setId(sid.nextShort());
        user.setMobile(mobile);
        user.setNickname("user_" + DesensitizationUtil.commonDisplay(mobile));
        user.setFace(" ");
        user.setBirthday(DateUtil.stringToDate("2000-01-01"));
        user.setSex(Sex.secret.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);
        user.setTotalIncome(0);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        appUserMapper.insert(user);
        return user;
    }

    @Transactional
    @Override
    public void updateUser(UpdateUserInfoBO updateUserInfoBO) {
        AppUser user = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO, user);
        user.setUpdatedTime(new Date());
        user.setActiveStatus(UserStatus.ACTIVE.type);

        int result = appUserMapper.updateByPrimaryKeySelective(user);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
    }

}
