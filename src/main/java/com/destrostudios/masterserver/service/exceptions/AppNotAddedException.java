package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class AppNotAddedException extends BaseException {

    public AppNotAddedException() {
        super("APP_NOT_ADDED", "App not added", HttpStatus.FORBIDDEN);
    }
}
