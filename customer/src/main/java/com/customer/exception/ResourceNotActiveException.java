package com.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotActiveException extends RuntimeException{

    public ResourceNotActiveException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not active with the given input data %s: '%s",resourceName,fieldName,fieldValue));
    }
}
