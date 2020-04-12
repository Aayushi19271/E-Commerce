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

    //HTTP STATUS 500 - INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions
            (Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,new Date(), ex.getMessage(),
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //HTTP STATUS 404 - USER NOT FOUND
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundExceptions
            (UserNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(),"User Not Found.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 - TOKEN NOT FOUND
    @ExceptionHandler(TokenNotFoundException.class)
    public final ResponseEntity<Object> handleTokenNotFoundException
    (TokenNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(), "Token Not Found.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 - Address Not Found Exception
    @ExceptionHandler(AddressNotFoundException.class)
    public final ResponseEntity<Object> handleAddressNotFoundException
    (AddressNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(), "Address Not Found.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }


    //HTTP STATUS 400 BAD REQUEST - VALIDATION FAILED
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,new Date(),
                "Validation Failed.", errors.toString());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 405 METHOD_NOT_ALLOWED
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED,new Date(),
                        "The Request Method Does Not Support.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    //HTTP STATUS 302 FOUND - When the user's emailID already Exists
    @ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<Object> handleUserAlreadyExistsExceptions
            (UserAlreadyExistsException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.FOUND,new Date(), "User Already Exists.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FOUND);
    }

    //HTTP STATUS 502 BAD_GATEWAY - Failed to send Mail
    @ExceptionHandler(MailSendFailedException.class)
    public final ResponseEntity<Object> handleMailSendFailedException
    (MailSendFailedException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_GATEWAY,new Date(), "Failed To Send Mail.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_GATEWAY);
    }

    //HTTP STATUS 400 BAD REQUEST - Password Does Not Match Exception
    @ExceptionHandler(PasswordDoesNotMatchException.class)
    public final ResponseEntity<Object> handlePasswordDoesNotMatchException
    (PasswordDoesNotMatchException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(), "Password And Confirm Password Should Match.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 400 BAD REQUEST - User Active Exception
    @ExceptionHandler(UserActiveException.class)
    public final ResponseEntity<Object> handleUserActiveException
    (UserActiveException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(),
                        "User Active Exception.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 400 BAD REQUEST - User Deactive Exception
    @ExceptionHandler(UserDeactiveException.class)
    public final ResponseEntity<Object> handleUserDeactiveException
    (UserDeactiveException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(),
                        "User De-active Exception.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    //HTTP STATUS 400 BAD REQUEST - Token Expired Exception
    @ExceptionHandler(TokenExpiredException.class)
    public final ResponseEntity<Object> handleTokenExpiredException
    (TokenExpiredException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(),
                        "Token Expired Exception.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


}
