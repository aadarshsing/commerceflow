package com.gatewayServer.exception;


import com.gatewayServer.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
@Order(-2)
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleResponseStatusException(
            ResponseStatusException exception,
            ServerWebExchange exchange) {

        logger.error(
                "Gateway exception. path={}, exceptionType={}, message={}",
                exchange.getRequest().getPath().value(),
                exception.getClass().getName(),
                exception.getMessage(),
                exception
        );

        HttpStatusCode status = exception.getStatusCode();

        String message = exception.getReason() != null
                ? exception.getReason()
                : exception.getMessage();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                exchange.getRequest().getPath().value(),
                status,
                message != null
                        ? message
                        : "An unexpected error occurred",
                LocalDateTime.now(),
                null
        );

        return Mono.just(
                ResponseEntity
                        .status(status)
                        .body(errorResponseDto)
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleGlobalException(
            Exception exception,
            ServerWebExchange exchange) {

        logger.error(
                "Gateway exception. path={}, exceptionType={}, message={}",
                exchange.getRequest().getPath().value(),
                exception.getClass().getName(),
                exception.getMessage(),
                exception
        );

        String message = exception.getMessage();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                exchange.getRequest().getPath().value(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                message != null ? message : "An unexpected error occurred",
                LocalDateTime.now(),
                null
        );


        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(errorResponseDto)
        );
    }
}