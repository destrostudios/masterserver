package com.destrostudios.masterserver.service.annotations;

import com.destrostudios.masterserver.service.exceptions.BaseException;
import jakarta.transaction.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Transactional(rollbackOn = BaseException.class)
public @interface BaseTransactional {
}
