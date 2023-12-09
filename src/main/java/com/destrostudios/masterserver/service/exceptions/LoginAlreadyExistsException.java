package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class LoginAlreadyExistsException extends BaseException {

    public LoginAlreadyExistsException() {
        super("LOGIN_ALREADY_EXISTS", "Login already exists", HttpStatus.FORBIDDEN);
    }
}
