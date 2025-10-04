package com.destrostudios.masterserver.service.exceptions;

import org.springframework.http.HttpStatus;

public class NotAHighscoreException extends BaseException {

    public NotAHighscoreException() {
        super("NOT_A_HIGHSCORE", "Not a highscore", HttpStatus.CONFLICT);
    }
}
