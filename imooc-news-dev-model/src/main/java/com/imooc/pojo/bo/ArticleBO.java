package com.imooc.pojo.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class ArticleBO {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotBlank
    @Length(max = 9999)
    private String content;

    @NotNull
    private Integer categoryId;

    @NotNull
    @Min(value = 1)
    @Max(value = 2)
    private Integer articleType;
    private String articleCover;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer isAppoint;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") // 前端日期字符串传到后端后，转换为Date类型
    private Date publishTime;

    @NotBlank
    private String publishUserId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    public Integer getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(Integer isAppoint) {
        this.isAppoint = isAppoint;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "ArticleBO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", categoryId=" + categoryId +
                ", articleType=" + articleType +
                ", articleCover='" + articleCover + '\'' +
                ", isAppoint=" + isAppoint +
                ", publishTime=" + publishTime +
                ", publishUserId='" + publishUserId + '\'' +
                '}';
    }
}