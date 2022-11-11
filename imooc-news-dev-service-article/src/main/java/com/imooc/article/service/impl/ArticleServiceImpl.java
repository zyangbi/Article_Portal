package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.GraceException;
import com.imooc.pojo.Article;
import com.imooc.pojo.bo.ArticleBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {
    @Autowired
    private Sid sid;
    @Autowired
    private ArticleMapper articleMapper;

    @Transactional
    @Override
    public void createArticle(ArticleBO articleBO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleBO, article);
        article.setId(sid.nextShort());
        article.setCategoryId(articleBO.getCategoryId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setReadCounts(0);
        article.setCommentCounts(0);
        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        if (article.getIsAppoint() == YesOrNo.YES.type) {
            article.setPublishTime(articleBO.getPublishTime());
        } else if (article.getIsAppoint() == YesOrNo.NO.type) {
            article.setPublishTime(new Date());
        }

        int result = articleMapper.insert(article);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
    }

    @Transactional
    @Override
    public void updateIsAppointToPublish() {
        articleMapper.updateIsAppointToPublish();
    }

    @Override
    public PagedGridResult getArticleList(String userId, String keyword, Integer status,
                                          Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("publishUserId", userId);

        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }

        if (status != null && status.equals(12)) {
            criteria.andEqualTo("articleStatus", 1).orEqualTo("articleStatus", 2);
        } else if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        criteria.andEqualTo("isDelete", YesOrNo.NO.type);

        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("publishTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("publishTime", endDate);
        }

        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        return setPagedGridResult(list);
    }

    @Transactional
    @Override
    public void deleteArticle(String userId, String articleId) {
        Example example = getArticleExample(userId, articleId);
        Article article = new Article();
        // set is_delete to YES
        article.setIsDelete(YesOrNo.YES.type);

        int result = articleMapper.updateByExampleSelective(article, example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }

    @Transactional
    @Override
    public void withdrawArticle(String userId, String articleId) {
        Example example = getArticleExample(userId, articleId);
        Article article = new Article();
        article.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);

        int result = articleMapper.updateByExampleSelective(article, example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }

    private Example getArticleExample(String userId, String articleId) {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        criteria.andEqualTo("id", articleId);
        return articleExample;
    }

}
