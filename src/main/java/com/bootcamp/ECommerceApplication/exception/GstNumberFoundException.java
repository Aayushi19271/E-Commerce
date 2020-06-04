package com.bootcamp.ECommerceApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class GstNumberFoundException extends RuntimeException{
    public GstNumberFoundException(String message) {
        super(message);
    }
}
