package com.javvathehutt.hic_gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice("com.javvathehutt.hic_gateway")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse monthValidationExceptionHandler(DateTimeParseException e) {
        log.error("Unsupported name of month", e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse fieldValidationExceptionHandler(BadRequestException e) {
        log.error("unsupported value", e);
        return new ErrorResponse(e.getMessage());
    }
}
