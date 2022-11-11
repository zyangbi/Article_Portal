package com.imooc.article.service;

import com.imooc.pojo.bo.ArticleBO;
import com.imooc.utils.PagedGridResult;

import java.util.Date;

public interface ArticleService {

    public void createArticle(ArticleBO articleBO);

    public void updateIsAppointToPublish();

    public PagedGridResult getArticleList(String userId, String keyword, Integer status,
                                          Date startDate, Date endDate, Integer page, Integer pageSize);

    public void deleteArticle(String userId, String articleId);

    public void withdrawArticle(String userId, String articleId);

}
