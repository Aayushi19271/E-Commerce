package com.bootcamp.ECommerceApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class UserAlreadyExistsException  extends RuntimeException{

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

