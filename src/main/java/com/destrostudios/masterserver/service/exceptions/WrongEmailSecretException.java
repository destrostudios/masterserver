package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class WrongEmailSecretException extends BaseException {

    public WrongEmailSecretException() {
        super("WRONG_EMAIL_SECRET", "Wrong email secret", HttpStatus.UNAUTHORIZED);
    }
}
