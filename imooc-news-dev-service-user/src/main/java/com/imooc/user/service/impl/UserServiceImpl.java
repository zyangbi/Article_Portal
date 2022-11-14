package com.imooc.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends BaseService implements UserService {

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

    @Override
    public PagedGridResult getUserList(String nickname, Integer status, Date startDate,
                                       Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();

        // set query conditions
        if (StringUtils.isNotBlank(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }
        if (UserStatus.isUserStatusValid(status)) {
            criteria.andEqualTo("ActiveStatus", status);
        }
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createdTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createdTime", endDate);
        }

        // return paged result
        PageHelper.startPage(page, pageSize);
        List<AppUser> list = appUserMapper.selectByExample(example);
        return setPagedGridResult(list);
    }

    @Transactional
    @Override
    public void updateUserStatus(String id, Integer status) {
        AppUser user = new AppUser();
        user.setId(id);
        user.setActiveStatus(status);
        appUserMapper.updateByPrimaryKeySelective(user);
    }

}
