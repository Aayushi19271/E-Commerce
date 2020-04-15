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
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(),"User Not Found!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 - TOKEN NOT FOUND
    @ExceptionHandler(TokenNotFoundException.class)
    public final ResponseEntity<Object> handleTokenNotFoundException
    (TokenNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(), "Token Not Found!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 - Address Not Found Exception
    @ExceptionHandler(AddressNotFoundException.class)
    public final ResponseEntity<Object> handleAddressNotFoundException
    (AddressNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(), "Address Not Found!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 - Parent Category Not Found Exception
    @ExceptionHandler(ParentCategoryNotFoundException.class)
    public final ResponseEntity<Object> handleParentCategoryNotFoundException
    (ParentCategoryNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(), "Parent Category Not Found!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 - Category Not Found Exception
    @ExceptionHandler(CategoryNotFoundException.class)
    public final ResponseEntity<Object> handleCategoryNotFoundException
    (CategoryNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(), "Category Not Found!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 - Category Metadata Field Not Found Exception
    @ExceptionHandler(CategoryMetadataFieldNotFoundException.class)
    public final ResponseEntity<Object> handleCategoryMetadataFieldNotFoundException
    (CategoryMetadataFieldNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(), "Category Metadata Field Not Found!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //HTTP STATUS 404 - Product Not Found Exception
    @ExceptionHandler(ProductNotFoundException.class)
    public final ResponseEntity<Object> handleProductNotFoundException
    (ProductNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND,new Date(), "Product Not Found!",
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
                "Validation Failed!", errors.toString());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 405 METHOD_NOT_ALLOWED
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED,new Date(),
                        "The Request Method Does Not Support!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    //HTTP STATUS 302 FOUND - User's  already Exists
    @ExceptionHandler(UserFoundException.class)
    public final ResponseEntity<Object> handleUserFoundException
            (UserFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.FOUND,new Date(), "User Already Exist!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FOUND);
    }

    //HTTP STATUS 302 FOUND - Category Metadata Field Values Found Exception
    @ExceptionHandler(CategoryMetadataFieldValuesFoundException.class)
    public final ResponseEntity<Object> handleCategoryMetadataFieldValuesFoundException
    (CategoryMetadataFieldValuesFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.FOUND,new Date(), "Category Metadata Field Values Already Exist!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FOUND);
    }


    //HTTP STATUS 302 FOUND - Metadata Field Found Exception
    @ExceptionHandler(MetadataFieldFoundException.class)
    public final ResponseEntity<Object> handleMetadataFieldExistsException
    (MetadataFieldFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.FOUND,new Date(), "Metadata Field Already Exists!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FOUND);
    }

    //HTTP STATUS 302 FOUND - Category Found Exception
    @ExceptionHandler(CategoryFoundException.class)
    public final ResponseEntity<Object> handleMetadataFieldExistsException
    (CategoryFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.FOUND,new Date(), "Category Already Exists!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FOUND);
    }

    //HTTP STATUS 302 FOUND - Product Found Exception
    @ExceptionHandler(ProductFoundException.class)
    public final ResponseEntity<Object> handleProductFoundException
    (ProductFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.FOUND,new Date(), "Product Already Exists!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FOUND);
    }

    //HTTP STATUS 502 BAD_GATEWAY - Failed to send Mail
    @ExceptionHandler(MailSendFailedException.class)
    public final ResponseEntity<Object> handleMailSendFailedException
    (MailSendFailedException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_GATEWAY,new Date(), "Failed To Send Mail!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_GATEWAY);
    }

    //HTTP STATUS 400 BAD REQUEST - Password Does Not Match Exception
    @ExceptionHandler(PasswordDoesNotMatchException.class)
    public final ResponseEntity<Object> handlePasswordDoesNotMatchException
    (PasswordDoesNotMatchException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(), "Password And Confirm Password Does Not Match!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 400 BAD REQUEST - Category Not LeafNode Exception
    @ExceptionHandler(CategoryNotLeafNodeException.class)
    public final ResponseEntity<Object> handleCategoryNotLeafNodeException
    (CategoryNotLeafNodeException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(), "Category Not A Leaf Node!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 400 BAD REQUEST - User Active Exception
    @ExceptionHandler(UserActiveException.class)
    public final ResponseEntity<Object> handleUserActiveException
    (UserActiveException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(),
                        "User Already Activated!.",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 400 BAD REQUEST - Product Active Exception
    @ExceptionHandler(ProductActiveException.class)
    public final ResponseEntity<Object> handleProductActiveException
    (ProductActiveException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(),
                        "Product Already Activated!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    //HTTP STATUS 400 BAD REQUEST - Product De-Active Exception
    @ExceptionHandler(ProductDeactiveException.class)
    public final ResponseEntity<Object> handleProductDeactiveException
    (ProductDeactiveException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(),
                        "Product Already De-Activated!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    //HTTP STATUS 400 BAD REQUEST - User De-active Exception
    @ExceptionHandler(UserDeactiveException.class)
    public final ResponseEntity<Object> handleUserDeactiveException
    (UserDeactiveException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(),
                        "User Already De-Activated!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    //HTTP STATUS 400 BAD REQUEST - Token Expired Exception
    @ExceptionHandler(TokenExpiredException.class)
    public final ResponseEntity<Object> handleTokenExpiredException
    (TokenExpiredException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( HttpStatus.BAD_REQUEST,new Date(),
                        "Token Expired!",
                        request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


}
