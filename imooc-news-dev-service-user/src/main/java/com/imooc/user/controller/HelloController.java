package com.imooc.user.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.utils.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {
    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Object hello() {
        logger.info("info");
        return GraceJSONResult.ok();
    }
}
