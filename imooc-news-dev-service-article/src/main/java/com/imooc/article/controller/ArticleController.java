package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.ArticleBO;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisOperator redis;

    @Override
    public GraceJSONResult createArticle(ArticleBO articleBO, BindingResult result) {
        if (result.hasErrors()) {
            return GraceJSONResult.errorMap(getErrorMap(result));
        }

        // check if category is valid
        String categoryListJson = redis.get(REDIS_CATEGORY);
        List<Category> categoryList = JsonUtils.jsonToList(categoryListJson, Category.class);
        if (!IsArticleCategoryValid(articleBO, categoryList)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }

        // create new aritcle
        articleService.createArticle(articleBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getArticleList(String userId, String keyword, Integer status,
                                          Date startDate, Date endDate, Integer page, Integer pageSize) {
        if (page == null) {
            page = PAGE_DEFAULT;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE_DEFAULT;
        }

        PagedGridResult result = articleService.getArticleList(userId, keyword, status, startDate, endDate, page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult deleteArticle(String userId, String articleId) {
        articleService.deleteArticle(userId, articleId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult withdrawArticle(String userId, String articleId) {
        articleService.withdrawArticle(userId, articleId);
        return GraceJSONResult.ok();
    }

    private boolean IsArticleCategoryValid(ArticleBO articleBO, List<Category> categoryList) {
        Integer categoryId = articleBO.getCategoryId();
        boolean result = false;
        for (Category category : categoryList) {
            if (category.getId() == categoryId) {
                result = true;
            }
        }
        return result;
    }

}
