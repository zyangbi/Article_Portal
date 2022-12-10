package com.imooc.api.controller.article;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@RequestMapping("/article/html")
@Api(value = "ArticleHtmlControllerApi")
public interface ArticleHtmlControllerApi {

    @GetMapping("/download")
    @ApiOperation(value = "download article HTML from GridFS", httpMethod = "GET")
    public Integer download(@RequestParam @NotNull String articleId,
                            @RequestParam @NotNull String articleMongoId) throws Exception;

    @GetMapping("/delete")
    @ApiOperation(value = "delete html", httpMethod = "GET")
    public Integer delete(@RequestParam @NotNull String articleId) throws Exception;

}