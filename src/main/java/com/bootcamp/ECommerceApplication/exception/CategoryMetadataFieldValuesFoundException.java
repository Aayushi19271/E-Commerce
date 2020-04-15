package com.bootcamp.ECommerceApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class CategoryMetadataFieldValuesFoundException extends RuntimeException{
    public CategoryMetadataFieldValuesFoundException(String message) {
        super(message);
    }
}
