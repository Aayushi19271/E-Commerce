package com.bootcamp.ECommerceApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParentCategoryNotFoundException extends RuntimeException{
    public ParentCategoryNotFoundException(String message) {
        super(message);
    }
}
