package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException() {
        super("EMAIL_ALREADY_EXISTS", "E-Mail already exists", HttpStatus.FORBIDDEN);
    }
}
