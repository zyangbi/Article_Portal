package com.imooc.user.service;

import com.imooc.enums.Sex;
import com.imooc.pojo.vo.ProvinceRatioVO;
import com.imooc.utils.PagedGridResult;

import java.util.Set;

public interface FanService {

    public boolean isFollower(String writerId, String fanId);

    public void follow(String writerId, String fanId);

    public void unfollow(String writerId, String fanId);

    public PagedGridResult getFanList(String writerId, Integer page, Integer pageSize);

    public int getCountBySex(String writerId, Sex sex);

    public ProvinceRatioVO getCountByProvince(String writerId, String province);

    public Set<String> getWriterSet();

    public Set<String> getFanSet();

    public void updateFanCount(String writerId);

    public void updateFollowCount(String fanId);

}
