package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class AppAlreadyAddedException extends BaseException {

    public AppAlreadyAddedException() {
        super("APP_ALREADY_ADDED", "App already added", HttpStatus.FORBIDDEN);
    }
}
