package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException() {
        super("EMAIL_ALREADY_EXISTS", "Email already exists", HttpStatus.FORBIDDEN);
    }
}
