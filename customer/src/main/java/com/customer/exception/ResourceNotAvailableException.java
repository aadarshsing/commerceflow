package com.customer.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotAvailableException extends RuntimeException{

    public  ResourceNotAvailableException(String resourceName, String fieldName, String fieldValue){
        super(String.format("%s not available with the given input data %s: '%s",resourceName,fieldName,fieldValue));
    }
}
