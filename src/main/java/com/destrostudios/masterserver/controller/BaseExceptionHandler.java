package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.model.ExceptionDto;
import com.destrostudios.masterserver.service.exceptions.BaseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { BaseException.class })
    protected ResponseEntity<Object> handleBaseException(BaseException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ExceptionDto("destrostudios", ex.getCode(), ex.getMessage()), new HttpHeaders(), ex.getHttpStatus(), request);
    }
}
