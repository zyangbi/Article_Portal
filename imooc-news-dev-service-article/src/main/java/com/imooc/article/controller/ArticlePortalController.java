package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticlePortalControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.IndexArticleVO;
import com.imooc.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private RedisOperator redis;

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

    @Override
    public GraceJSONResult getArticleDetail(String articleId) {
        // 1. Get article info
        Article article = articleService.getArticle(articleId);

        // 2. Get publisher name
        List<Article> articleList = new ArrayList<>();
        articleList.add(article);
        List<AppUserVO> appUserVOList = getPublisherList(articleList);
        String publisherUserName = appUserVOList.get(0).getNickname();

        // 3. Get read count
        Integer readCount = getCountFromRedis(REDIS_READ_COUNT + articleId);

        // 4. Return articleDetailVO
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(article, articleDetailVO);
        articleDetailVO.setPublishUserName(publisherUserName);
        articleDetailVO.setReadCounts(readCount);
        return GraceJSONResult.ok(articleDetailVO);
    }

    @Override
    public Integer getReadCount(String articleId) {
        return getCountFromRedis(REDIS_READ_COUNT + articleId);
    }

    @Override
    public GraceJSONResult increaseReadCount(String articleId, HttpServletRequest request) {
        redis.increment(REDIS_READ_COUNT + articleId, 1);
        String ip = IPUtil.getRequestIp(request);
        redis.set(REDIS_READ_IP + ip, ip);
        return GraceJSONResult.ok();
    }

    private PagedGridResult getArticleVOList(PagedGridResult pagedGridResult) {
        // 1. Get article list
        List<Article> articleList = (List<Article>) pagedGridResult.getRows();
        // 2. Get publisher list
        List<AppUserVO> publisherList = getPublisherList(articleList);
        // 3. Get read count list
        List<Integer> readCountList = getReadCountList(articleList);

        // 4. Merge lists into indexArticleVO list
        List<IndexArticleVO> indexArticleVOList = new ArrayList<>();
        for (int i = 0; i < articleList.size(); ++i) {
            Article article = articleList.get(i);

            IndexArticleVO indexArticleVO = new IndexArticleVO();
            // set article
            BeanUtils.copyProperties(article, indexArticleVO);
            // set publisher
            for (AppUserVO publisher : publisherList) {
                if (publisher.getId().equalsIgnoreCase(article.getPublishUserId())) {
                    indexArticleVO.setPublisherVO(publisher);
                }
            }
            // set read count
            indexArticleVO.setReadCounts(readCountList.get(i));

            indexArticleVOList.add(indexArticleVO);
        }

        pagedGridResult.setRows(indexArticleVOList);
        return pagedGridResult;
    }

    private List<AppUserVO> getPublisherList(List<Article> articleList) {
        // 1. Get publisher id set
        Set<String> publisherIdSet = new HashSet<>();
        for (Article article : articleList) {
            publisherIdSet.add(article.getPublishUserId());
        }

        // 2. Get publisher list by requesting UserController
        String publisherVOListUrl = "http://user.imoocnews.com:8003/user/queryByIds?userIds="
                + JsonUtils.objectToJson(publisherIdSet);
        GraceJSONResult responseBody = restTemplate.getForEntity(publisherVOListUrl, GraceJSONResult.class).getBody();

        List<AppUserVO> publisherVOList = null;
        if (responseBody.getStatus() == 200) {
            String publisherVOJson = JsonUtils.objectToJson(responseBody.getData());
            publisherVOList = JsonUtils.jsonToList(publisherVOJson, AppUserVO.class);
        }
        return publisherVOList;
    }

    private List<Integer> getReadCountList(List<Article> articleList) {
        // 1. Get read count from Redis in batch
        List<String> keyList = new ArrayList<>();
        for (Article article : articleList) {
            keyList.add(REDIS_READ_COUNT + article.getId());
        }
        List<String> countStrList = redis.mget(keyList);

        // 2. Convert string value to integer
        List<Integer> countList = new ArrayList<>();
        for (String countStr : countStrList) {
            if (StringUtils.isBlank(countStr)) {
                countList.add(0);
            } else {
                countList.add(Integer.valueOf(countStr));
            }
        }
        return countList;
    }

}
