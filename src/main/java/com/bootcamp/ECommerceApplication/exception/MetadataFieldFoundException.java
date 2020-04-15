package com.bootcamp.ECommerceApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class MetadataFieldFoundException extends RuntimeException{
    public MetadataFieldFoundException(String message) {
        super(message);
    }
}
