package com.imooc.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "HelloController", tags = {"xxx"})
public interface HelloControllerApi {
    @GetMapping("/hello")
    @ApiOperation(value = "hello method", notes = "hello method", httpMethod = "GET")
    public Object hello();

}
