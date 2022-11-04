package com.imooc.exception;

import com.imooc.enums.ResponseStatusEnum;

public class GraceException {

    public static void displayMyCustomException(ResponseStatusEnum responseStatusEnum) {
        throw new MyCustomException(responseStatusEnum);
    }

}
