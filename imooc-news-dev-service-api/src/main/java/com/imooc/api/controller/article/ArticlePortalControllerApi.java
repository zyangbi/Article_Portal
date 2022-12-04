package com.imooc.api.controller.article;

import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

@Api(value = "ArticlePortalController")
@RequestMapping("/portal/article")
public interface ArticlePortalControllerApi {

    @GetMapping("/list")
    @ApiOperation(value = "article list on portal", httpMethod = "GET")
    public GraceJSONResult getArticleList(@RequestParam String keyword,
                                          @RequestParam Integer category,
                                          @ApiParam(required = false)
                                          @RequestParam Integer page,
                                          @ApiParam(required = false)
                                          @RequestParam Integer pageSize);

    @GetMapping("/queryArticleListOfWriter")
    @ApiOperation(value = "article list of a writer", httpMethod = "GET")
    public GraceJSONResult getArticleListByWriter(@NotBlank String writerId,
                                                  @ApiParam(required = false)
                                                  @RequestParam Integer page,
                                                  @ApiParam(required = false)
                                                  @RequestParam Integer pageSize);

    @GetMapping("/detail")
    @ApiOperation(value = "get article detail", httpMethod = "GET")
    public GraceJSONResult getArticleDetail(@RequestParam @NotBlank String articleId);

    @PostMapping("/readArticle")
    @ApiOperation(value = "increase article read count", httpMethod = "POST")
    public GraceJSONResult increaseReadCount(@RequestParam String articleId, HttpServletRequest request);

}
