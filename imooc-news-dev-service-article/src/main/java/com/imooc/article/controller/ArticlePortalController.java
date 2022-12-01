package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticlePortalControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.IndexArticleVO;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GraceJSONResult getArticleList(String keyword, Integer category, Integer page, Integer pageSize) {
        page = setPage(page);
        pageSize = setPageSize(pageSize);

        PagedGridResult articleList = articleService.getArticleList(keyword, category, page, pageSize);
        PagedGridResult articleVOList = getArticleVOList(articleList);
        return GraceJSONResult.ok(articleVOList);
    }

    @Override
    public GraceJSONResult getArticleListByWriter(String writerId, Integer page, Integer pageSize) {
        page = setPage(page);
        pageSize = setPageSize(pageSize);

        PagedGridResult articleList = articleService.getArticleListByPublisher(writerId, page, pageSize);
        PagedGridResult articleVOList = getArticleVOList(articleList);
        return GraceJSONResult.ok(articleVOList);
    }

    private PagedGridResult getArticleVOList(PagedGridResult pagedGridResult) {
        // 1. Get article list
        List<Article> articleList = (List<Article>) pagedGridResult.getRows();

        Set<String> idSet = new HashSet<>();
        for (Article article : articleList) {
            idSet.add(article.getPublishUserId());
        }

        // 2. Get publisher list
        String publisherVOListUrl = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> responseEntity = restTemplate.getForEntity(publisherVOListUrl, GraceJSONResult.class);
        GraceJSONResult responseBody = responseEntity.getBody();

        List<AppUserVO> publisherVOList = null;
        if (responseBody.getStatus() == 200) {
            String publisherVOJson = JsonUtils.objectToJson(responseBody.getData());
            publisherVOList = JsonUtils.jsonToList(publisherVOJson, AppUserVO.class);
        }

        // 3. Merge article and userVO lists into indexArticleVOList
        List<IndexArticleVO> indexArticleVOList = new ArrayList<>();
        for (Article article : articleList) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            // copy article
            BeanUtils.copyProperties(article, indexArticleVO);
            // copy publisherVO
            for (AppUserVO publisherVO : publisherVOList) {
                if (publisherVO.getId().equalsIgnoreCase(article.getPublishUserId())) {
                    indexArticleVO.setPublisherVO(publisherVO);
                }
            }
            indexArticleVOList.add(indexArticleVO);
        }

        pagedGridResult.setRows(indexArticleVOList);
        return pagedGridResult;
    }

}
