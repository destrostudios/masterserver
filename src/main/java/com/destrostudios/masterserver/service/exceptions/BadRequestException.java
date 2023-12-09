package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException() {
        super("BAD_REQUEST", "Bad request", HttpStatus.BAD_REQUEST);
    }
}
