package com.bootcamp.ECommerceApplication.exception.handler;

import com.bootcamp.ECommerceApplication.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    //HTTP STATUS 500 INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleAllExceptions
            (Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(),
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //HTTP STATUS 404 USER NOT FOUND
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundExceptions
            (UserNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(),"User Not Found.",
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 TOKEN NOT FOUND
    @ExceptionHandler(TokenNotFoundException.class)
    public final ResponseEntity<Object> handleTokenNotFoundException
    (TokenNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), "Token Not Found.",
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }


    //HTTP STATUS 400 BAD REQUEST
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                "Validation Failed.", errors.toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 405 METHOD_NOT_ALLOWED
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), "The Request Method Does Not Support.",
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    //HTTP STATUS 302 FOUND - When the user's emailID already Exists
    @ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<Object> handleUserAlreadyExistsExceptions
            (UserAlreadyExistsException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), "User Already Exists.",
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.FOUND);
    }

    //HTTP STATUS 403 FOUND - If the logged-in user does not have the required authorization to access the APIs
    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> handleAllUnauthorizedExceptions
    (UserAlreadyExistsException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(),"Full authentication is required to access this resource.",
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN);
    }
}
