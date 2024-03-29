package com.imooc.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.service.AdminUserService;
import com.imooc.api.service.BaseService;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.exception.GraceException;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AdminUserServiceImpl extends BaseService implements AdminUserService {
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private Sid sid;

    @Override
    public AdminUser getAdminByUsername(String username) {
        Example example = new Example(AdminUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        AdminUser user = adminUserMapper.selectOneByExample(example);
        return user;
    }

    @Override
    @Transactional
    public void createAdmin(AdminBO adminBO) {
        AdminUser admin = new AdminUser();
        admin.setId(sid.nextShort());
        admin.setUsername(adminBO.getUsername());
        admin.setAdminName(adminBO.getAdminName());
        if (StringUtils.isNotBlank(adminBO.getPassword())) {
            admin.setPassword(BCrypt.hashpw(adminBO.getPassword(), BCrypt.gensalt()));
        }
        if (StringUtils.isNotEmpty(adminBO.getFaceId())) {
            admin.setFaceId(adminBO.getFaceId());
        }
        admin.setCreatedTime(new Date());
        admin.setUpdatedTime(new Date());

        int result = adminUserMapper.insert(admin);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public PagedGridResult getAdminList(Integer pageNum, Integer pageSize) {
        Example example = new Example(AdminUser.class);
        example.orderBy("createdTime").desc();
        PageHelper.startPage(pageNum, pageSize);
        List<AdminUser> adminList = adminUserMapper.selectByExample(example);
        return setPagedGridResult(adminList);
    }

}
