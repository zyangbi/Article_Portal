package com.imooc.exception;

import com.imooc.utils.GraceJSONResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GraceExceptionHandler {

    // tell the config to handle MyCustomException
    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJSONResult MyCustomExceptionHandler(MyCustomException myCustomException) {
        String errorMsg = myCustomException.getMessage();
        System.out.println(errorMsg);
        return GraceJSONResult.errorCustom(myCustomException.getResponseStatusEnum());
    }

}
