package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class TooManyEmailRequestsException extends BaseException {

    public TooManyEmailRequestsException() {
        super("TOO_MANY_EMAIL_REQUESTS", "Too many e-mail requests", HttpStatus.TOO_MANY_REQUESTS);
    }
}
