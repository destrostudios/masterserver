package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND);
    }
}
