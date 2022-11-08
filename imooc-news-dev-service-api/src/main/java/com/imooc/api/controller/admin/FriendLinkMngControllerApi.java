package com.imooc.api.controller.admin;

import com.imooc.pojo.bo.SaveFriendLinkBO;
import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@RequestMapping("/friendLinkMng")
@Api(value = "FriendLinkMngControllerApi")
public interface FriendLinkMngControllerApi {

    @PostMapping("/saveOrUpdateFriendLink")
    @ApiOperation(value = "save or update friend link", notes = "save or update friend link", httpMethod = "POST")
    public GraceJSONResult saveOrUpdateFriendLink(@RequestBody @Valid SaveFriendLinkBO saveFriendLinkBO, BindingResult result);

    @PostMapping("/getFriendLinkList")
    @ApiOperation(value = "get friend link list", notes = "get friend link list", httpMethod = "POST")
    public GraceJSONResult getFriendLinkList();

    @PostMapping("/delete")
    @ApiOperation(value = "delete friend link", notes = "delete friend link", httpMethod = "POST")
    public GraceJSONResult deleteFriendLink(@RequestParam String linkId);

}
