package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class AppNotFoundException extends BaseException {

    public AppNotFoundException() {
        super("APP_NOT_FOUND", "App not found", HttpStatus.NOT_FOUND);
    }
}
