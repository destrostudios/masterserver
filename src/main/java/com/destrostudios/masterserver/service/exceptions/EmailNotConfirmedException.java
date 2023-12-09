package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class EmailNotConfirmedException extends BaseException {

    public EmailNotConfirmedException() {
        super("EMAIL_NOT_CONFIRMED", "E-Mail not confirmed", HttpStatus.FORBIDDEN);
    }
}
