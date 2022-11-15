package com.imooc.admin.service;

import com.imooc.pojo.mo.FriendLinkMO;

import java.util.List;

public interface FriendLinkService {

    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    public List<FriendLinkMO> getFriendLinkList();

    public void deleteFriendLink(String id);

    public List<FriendLinkMO> getPortalFriendLinkList();

}
