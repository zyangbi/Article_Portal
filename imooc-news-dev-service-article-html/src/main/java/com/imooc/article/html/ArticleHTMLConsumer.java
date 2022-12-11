package com.imooc.article.html;

import com.imooc.api.config.RabbitMQConfig;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Component
public class ArticleHTMLConsumer {

    @Autowired
    private GridFSBucket gridFSBucket;
    @Value("${freemarker.html.article}")
    private String articlePath;

    // Listen to queue
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_DOWNLOAD_HTML})
    public void listenToMQ(String payload, Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();

        if (routingKey.equalsIgnoreCase("article.html.download")) {
            String articleId = payload.split(",")[0];
            String mongoFileId = payload.split(",")[1];
            try {
                download(articleId, mongoFileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (routingKey.equalsIgnoreCase("article.html.delete")) {
            String articleId = payload;
            try {
                delete(articleId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Integer download(String articleId, String articleMongoId) throws Exception {
        String path = articlePath + articleId + ".html";
        File file = new File(path);
        OutputStream outputStream = new FileOutputStream(file);
        gridFSBucket.downloadToStream(new ObjectId(articleMongoId), outputStream);
        return HttpStatus.OK.value();
    }

    private Integer delete(String articleId) throws Exception {
        String path = articlePath + articleId + ".html";
        File file = new File(path);
        file.delete();
        return HttpStatus.OK.value();
    }

}
