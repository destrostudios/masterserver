package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class AppFileNotFoundException extends BaseException {

    public AppFileNotFoundException() {
        super("APP_FILE_NOT_FOUND", "App file not found", HttpStatus.NOT_FOUND);
    }
}
