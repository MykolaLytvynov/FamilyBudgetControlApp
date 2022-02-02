package com.mykola.familybudgetcontrolapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class LimitException extends RuntimeException{
    public LimitException(String message) {
        super(message);
    }
}
