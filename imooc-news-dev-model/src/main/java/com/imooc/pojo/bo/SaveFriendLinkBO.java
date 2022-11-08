package com.imooc.pojo.bo;

import com.imooc.validate.CheckUrl;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SaveFriendLinkBO {
    private String id;
    @NotBlank(message = "friend link can not be null")
    private String linkName;
    @NotBlank(message = "friend link URL can not be null")
    @CheckUrl
    private String linkUrl;
    @NotNull(message = "isDelete")
    private Integer isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
