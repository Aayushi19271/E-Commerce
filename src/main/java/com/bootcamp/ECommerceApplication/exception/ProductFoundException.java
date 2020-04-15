package com.bootcamp.ECommerceApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class ProductFoundException  extends RuntimeException{
    public ProductFoundException(String message) {
        super(message);
    }
}
