package com.imooc.exception;

import com.imooc.enums.ResponseStatusEnum;

public class GraceException {

    // throws a MyCustomerException
    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new MyCustomException(responseStatusEnum);
    }

}
