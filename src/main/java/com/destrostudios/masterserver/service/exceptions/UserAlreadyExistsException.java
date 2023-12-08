package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseException {

    public UserAlreadyExistsException() {
        super("USER_ALREADY_EXISTS", "User already exists", HttpStatus.FORBIDDEN);
    }
}
