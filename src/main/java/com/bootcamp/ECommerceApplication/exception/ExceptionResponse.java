package com.bootcamp.ECommerceApplication.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ExceptionResponse {
    private HttpStatus statusCode;
    private Date timestamp;
    private String message;
    private String details;
    private boolean status = false;

//    public ExceptionResponse(Date timestamp, String message, String details) {
//        super();
//        this.timestamp = timestamp;
//        this.message = message;
//        this.details = details;
//    }

    public ExceptionResponse(HttpStatus statusCode, Date timestamp, String message, String details) {
        super();
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public HttpStatus getStatusCode() { return statusCode; }
    public Date getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }
    public String getDetails() {
        return details;
    }
    public boolean isStatus() { return status; }
}
