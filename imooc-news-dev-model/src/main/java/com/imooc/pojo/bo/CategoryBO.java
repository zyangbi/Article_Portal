package com.imooc.pojo.bo;

import javax.validation.constraints.NotBlank;

public class CategoryBO {
    private Integer id;
    @NotBlank(message = "category name can not be blank")
    private String name;
    private String oldName;
    @NotBlank(message = "category color can not be blank")
    private String tagColor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }
}