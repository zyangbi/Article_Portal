package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.exception.GraceException;
import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.ArticleBO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import com.mongodb.client.gridfs.GridFSBucket;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Sid sid;
    @Autowired
    private GridFSBucket gridFSBucket;

    @Value("${freemarker.html.article}")
    private String articlePath;

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

        // create article
        String articleId = sid.nextShort();
        articleService.createArticle(articleBO, articleId);
        try {
            // upload article HTML to GridFS
            String mongoFileId = createArticleHTMLToGridFS(articleId);
            articleService.updateMongoFileId(articleId, mongoFileId);
            // download article HTML to frontend
            downloadArticleHTMLToConsumer(articleId, mongoFileId);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        Article article = articleService.getArticle(articleId);
        // delete article HTML from consumer
        deleteArticleHTMLFromConsumer(articleId);
        // delete article HTML from GridFS
        gridFSBucket.delete(new ObjectId(article.getMongoFileId()));
        // delete article from MySQL
        articleService.deleteArticle(userId, articleId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult withdrawArticle(String userId, String articleId) {
        Article article = articleService.getArticle(articleId);
        deleteArticleHTMLFromConsumer(articleId);
        gridFSBucket.delete(new ObjectId(article.getMongoFileId()));
        articleService.withdrawArticle(userId, articleId);
        return GraceJSONResult.ok();
    }

    private String createArticleHTMLToGridFS(String articleId) throws Exception {
        // 1. Load Freemarker template
        Configuration cfg = new Configuration(Configuration.getVersion());
        String classpath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));
        Template template = cfg.getTemplate("detail.ftl", "utf-8");

        // 2. Get article detail data
        ArticleDetailVO articleDetailVO = getArticleDetailVO(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail", articleDetailVO);

        // 3. Upload HTML file to GridFS
        String htmlStr = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream htmlStream = IOUtils.toInputStream(htmlStr);
        ObjectId mongoFileId = gridFSBucket.uploadFromStream(articleDetailVO.getId() + ".html", htmlStream);
        return mongoFileId.toString();
    }

    private void downloadArticleHTMLToConsumer(String articleId, String articleMongoId) {
        String htmlUrl = "http://writer.imoocnews.com:8001/article/html/download?articleId="
                + articleId + "&articleMongoId=" + articleMongoId;
        Integer status = restTemplate.getForEntity(htmlUrl, Integer.class).getBody();
        if (status != HttpStatus.OK.value()) {
            GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    private void deleteArticleHTMLFromConsumer(String articleId) {
        String deleteUrl = "http://writer.imoocnews.com:8001/article/html/delete?articleId=" + articleId;
        Integer status = restTemplate.getForEntity(deleteUrl, Integer.class).getBody();
        if (status != HttpStatus.OK.value()) {
            GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    private ArticleDetailVO getArticleDetailVO(String articleId) {
        // Request ArticlePortalController.detail
        String url = "http://user.imoocnews.com:8001/portal/article/detail?articleId=" + articleId;
        GraceJSONResult responseBody = restTemplate.getForEntity(url, GraceJSONResult.class).getBody();
        ArticleDetailVO articleDetailVO = null;
        if (responseBody.getStatus() == 200) {
            String detailJson = JsonUtils.objectToJson(responseBody.getData());
            articleDetailVO = JsonUtils.jsonToPojo(detailJson, ArticleDetailVO.class);
        }
        return articleDetailVO;
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
