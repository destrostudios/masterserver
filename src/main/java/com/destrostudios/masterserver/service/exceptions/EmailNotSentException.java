package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class EmailNotSentException extends BaseException {

    public EmailNotSentException() {
        super("EMAIL_NOT_SENT", "E-Mail could not be sent", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
