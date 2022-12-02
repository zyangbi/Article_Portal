package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.FanControllerApi;
import com.imooc.enums.Sex;
import com.imooc.pojo.vo.FansCountsVO;
import com.imooc.pojo.vo.ProvinceRatioVO;
import com.imooc.user.service.FanService;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class FanController extends BaseController implements FanControllerApi {

    @Autowired
    private FanService fanService;

    @Override
    public GraceJSONResult isFollower(String writerId, String fanId) {
        boolean result = fanService.isFollower(writerId, fanId);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        fanService.follow(writerId, fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult unfollow(String writerId, String fanId) {
        fanService.unfollow(writerId, fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getFanList(String writerId, Integer page, Integer pageSize) {
        page = setPage(page);
        pageSize = setPageSize(pageSize);

        PagedGridResult result = fanService.getFanList(writerId, page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult getRatioOfSex(String writerId) {
        int manCount = fanService.getCountBySex(writerId, Sex.man);
        int womanCount = fanService.getCountBySex(writerId, Sex.woman);

        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(manCount);
        fansCountsVO.setWomanCounts(womanCount);
        return GraceJSONResult.ok(fansCountsVO);
    }

    @Override
    public GraceJSONResult getRatioOfProvince(String writerId) {
        List<ProvinceRatioVO> provinceRatioVOList = new ArrayList<>();
        for (String province : provinceList) {
            provinceRatioVOList.add(fanService.getCountByProvince(writerId, province));
        }
        return GraceJSONResult.ok(provinceRatioVOList);
    }

    @Override
    public GraceJSONResult updateRedisFanFollowCount() {
        Set<String> writerSet = fanService.getWriterSet();
        for (String writerId : writerSet) {
            fanService.updateFanCount(writerId);
        }

        Set<String> fanSet = fanService.getFanSet();
        for (String fanId : fanSet) {
            fanService.updateFollowCount(fanId);
        }

        return GraceJSONResult.ok();
    }


}
