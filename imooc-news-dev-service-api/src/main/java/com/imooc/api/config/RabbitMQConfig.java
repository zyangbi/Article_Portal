package com.imooc.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_ARTICLE = "exchange_article";
    public static final String QUEUE_DOWNLOAD_HTML = "queue_download_html";

    @Bean
    @Qualifier(EXCHANGE_ARTICLE)
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_ARTICLE).durable(true).build();
    }

    @Bean
    @Qualifier(QUEUE_DOWNLOAD_HTML)
    public Queue queue() {
        return new Queue(QUEUE_DOWNLOAD_HTML);
    }

    // bind exchange to queue
    @Bean
    public Binding binding(@Qualifier(EXCHANGE_ARTICLE) Exchange exchange,
                           @Qualifier(QUEUE_DOWNLOAD_HTML) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("article.html.*").noargs();
    }

}
