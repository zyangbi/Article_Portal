package com.imooc.article.controller;

import com.imooc.api.controller.article.ArticleHtmlControllerApi;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@RestController
public class ArticleHtmlController implements ArticleHtmlControllerApi {

    @Autowired
    private GridFSBucket gridFSBucket;
    @Value("${freemarker.html.article}")
    private String articlePath;

    @Override
    public Integer download(String articleId, String articleMongoId) throws Exception {
        String path = articlePath + articleId + ".html";
        File file = new File(path);
        OutputStream outputStream = new FileOutputStream(file);
        gridFSBucket.downloadToStream(new ObjectId(articleMongoId), outputStream);
        return HttpStatus.OK.value();
    }

    @Override
    public Integer delete(String articleId) throws Exception {
        String path = articlePath + articleId + ".html";
        File file = new File(path);
        file.delete();
        return HttpStatus.OK.value();
    }

}
