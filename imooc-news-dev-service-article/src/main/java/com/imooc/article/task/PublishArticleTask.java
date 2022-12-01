package com.imooc.article.task;

import com.imooc.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class PublishArticleTask {
    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void publishArticle() {
        articleService.updateIsAppointToPublish();
    }

}
