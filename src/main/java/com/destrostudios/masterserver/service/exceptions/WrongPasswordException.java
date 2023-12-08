package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class WrongPasswordException extends BaseException {

    public WrongPasswordException() {
        super("WRONG_PASSWORD", "Wrong password", HttpStatus.UNAUTHORIZED);
    }
}
