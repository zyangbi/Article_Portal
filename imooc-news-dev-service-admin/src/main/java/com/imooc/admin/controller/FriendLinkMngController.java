package com.imooc.admin.controller;

import com.imooc.admin.service.FriendLinkService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.FriendLinkMngControllerApi;
import com.imooc.pojo.bo.SaveFriendLinkBO;
import com.imooc.pojo.mo.FriendLinkMO;
import com.imooc.utils.GraceJSONResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class FriendLinkMngController extends BaseController implements FriendLinkMngControllerApi {

    @Autowired
    private FriendLinkService friendLinkService;

    @Override
    public GraceJSONResult saveOrUpdateFriendLink(SaveFriendLinkBO saveFriendLinkBO, BindingResult result) {
        if (result.hasErrors()) {
            GraceJSONResult.errorMap(getErrorMap(result));
        }

        FriendLinkMO friendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO, friendLinkMO);
        friendLinkMO.setCreateTime(new Date());
        friendLinkMO.setUpdateTime(new Date());

        friendLinkService.saveOrUpdateFriendLink(friendLinkMO);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getFriendLinkList() {
        List<FriendLinkMO> list = friendLinkService.getFriendLinkList();
        return GraceJSONResult.ok(list);
    }

    @Override
    public GraceJSONResult deleteFriendLink(String linkId) {
        friendLinkService.deleteFriendLink(linkId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getPortalFriendLinkList() {
        List<FriendLinkMO> list = friendLinkService.getPortalFriendLinkList();
        return GraceJSONResult.ok(list);
    }
}
