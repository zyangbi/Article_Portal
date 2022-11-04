package com.imooc.exception;

import com.imooc.utils.GraceJSONResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GraceExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJSONResult MyCustomExceptionHandler(MyCustomException myCustomException) {
        myCustomException.printStackTrace();
        return GraceJSONResult.errorCustom(myCustomException.getResponseStatusEnum());
    }

}
