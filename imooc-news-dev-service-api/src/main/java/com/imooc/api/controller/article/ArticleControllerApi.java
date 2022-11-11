package com.imooc.api.controller.article;

import com.imooc.pojo.bo.ArticleBO;
import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RequestMapping("/article")
@Api(value = "ArticleControllerApi")
public interface ArticleControllerApi {

    @PostMapping("/createArticle")
    @ApiOperation(value = "create article", notes = "create article", httpMethod = "POST")
    public GraceJSONResult createArticle(@RequestBody @Valid ArticleBO articleBO, BindingResult result);

    @PostMapping("/queryMyList")
    @ApiOperation(value = "get article list", notes = "get article list", httpMethod = "POST")
    public GraceJSONResult getArticleList(@RequestParam @NotBlank String userId,
                                          @RequestParam String keyword,
                                          @RequestParam Integer status,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                          @RequestParam Integer page,
                                          @RequestParam Integer pageSize);

    @PostMapping("/delete")
    @ApiOperation(value = "user delete article", notes = "user delete article", httpMethod = "POST")
    public GraceJSONResult deleteArticle(@RequestParam @NotNull String userId,
                                         @RequestParam @NotNull String articleId);

    @PostMapping("/withdraw")
    @ApiOperation(value = "user withdraw article", notes = "user withdraw article", httpMethod = "POST")
    public GraceJSONResult withdrawArticle(@RequestParam @NotNull String userId,
                                           @RequestParam @NotNull String articleId);

}
