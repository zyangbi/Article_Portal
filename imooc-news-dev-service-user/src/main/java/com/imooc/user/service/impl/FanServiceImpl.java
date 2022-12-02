package com.imooc.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.enums.Sex;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.Fan;
import com.imooc.pojo.vo.ProvinceRatioVO;
import com.imooc.user.mapper.FanMapper;
import com.imooc.user.service.FanService;
import com.imooc.user.service.UserService;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FanServiceImpl extends BaseService implements FanService {

    @Autowired
    private FanMapper fanMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private Sid sid;
    @Autowired
    private RedisOperator redis;

    @Override
    public boolean isFollower(String writerId, String fanId) {
        Fan fan = new Fan();
        fan.setWriterId(writerId);
        fan.setFanId(fanId);
        int count = fanMapper.selectCount(fan);
        return count > 0 ? true : false;
    }

    @Transactional
    @Override
    public void follow(String writerId, String fanId) {
        AppUser fanInfo = userService.getUser(fanId);

        Fan fan = new Fan();
        fan.setId(sid.nextShort());
        fan.setWriterId(writerId);
        fan.setFanId(fanId);
        fan.setFanNickname(fanInfo.getNickname());
        fan.setProvince(fanInfo.getProvince());
        fan.setSex(fanInfo.getSex());
        fanMapper.insert(fan);

        redis.increment(REDIS_FANS_COUNT + writerId, 1);
        redis.increment(REDIS_FOLLOW_COUNT + fanId, 1);
    }

    @Transactional
    @Override
    public void unfollow(String writerId, String fanId) {
        Fan fan = new Fan();
        fan.setWriterId(writerId);
        fan.setFanId(fanId);
        fanMapper.delete(fan);

        redis.decrement(REDIS_FANS_COUNT + writerId, 1);
        redis.decrement(REDIS_FOLLOW_COUNT + fanId, 1);
    }

    @Override
    public PagedGridResult getFanList(String writerId, Integer page, Integer pageSize) {
        Fan fan = new Fan();
        fan.setWriterId(writerId);

        PageHelper.startPage(page, pageSize);
        List<Fan> fanList = fanMapper.select(fan);
        return setPagedGridResult(fanList);
    }

    @Override
    public int getCountBySex(String writerId, Sex sex) {
        Fan fan = new Fan();
        fan.setWriterId(writerId);
        fan.setSex(sex.type);
        int count = fanMapper.selectCount(fan);
        return count;
    }

    @Override
    public ProvinceRatioVO getCountByProvince(String writerId, String province) {
        Fan fan = new Fan();
        fan.setWriterId(writerId);
        fan.setProvince(province);
        int count = fanMapper.selectCount(fan);

        ProvinceRatioVO provinceRatioVO = new ProvinceRatioVO();
        provinceRatioVO.setName(province);
        provinceRatioVO.setValue(count);
        return provinceRatioVO;
    }

    @Transactional
    @Override
    public void updateFanCount(String writerId) {
        Fan fan = new Fan();
        fan.setWriterId(writerId);
        Integer count = fanMapper.selectCount(fan);
        redis.set(REDIS_FANS_COUNT + writerId, count.toString());
    }

    @Transactional
    @Override
    public void updateFollowCount(String fanId) {
        Fan fan = new Fan();
        fan.setFanId(fanId);
        Integer count = fanMapper.selectCount(fan);
        redis.set(REDIS_FOLLOW_COUNT + fanId, count.toString());
    }

    @Override
    public Set<String> getWriterSet() {
        List<Fan> fanList = fanMapper.selectAll();

        Set<String> writerSet = new HashSet<>();
        for (Fan fan : fanList) {
            writerSet.add(fan.getWriterId());
        }
        return writerSet;
    }

    @Override
    public Set<String> getFanSet() {
        List<Fan> fanList = fanMapper.selectAll();

        Set<String> fanSet = new HashSet<>();
        for (Fan fan : fanList) {
            fanSet.add(fan.getFanId());
        }
        return fanSet;
    }
}
