package com.javvathehutt.hic_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ReadDataException extends RuntimeException {
    public ReadDataException(final String message) {
        super(message);
    }
}
