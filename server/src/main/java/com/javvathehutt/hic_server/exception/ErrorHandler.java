package com.javvathehutt.hic_server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestControllerAdvice("com.javvathehutt.hic_gateway")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse monthValidationExceptionHandler(HttpClientErrorException e) {
        log.error("bad request to isdayof", e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse monthValidationExceptionHandler(HttpServerErrorException e) {
        log.error("server isdayof problem", e);
        return new ErrorResponse(e.getMessage());
    }
}
