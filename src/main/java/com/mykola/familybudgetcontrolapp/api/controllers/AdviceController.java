package com.mykola.familybudgetcontrolapp.api.controllers;

import com.mykola.familybudgetcontrolapp.exception.LimitException;
import com.mykola.familybudgetcontrolapp.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(value = LimitException.class)
    public ResponseEntity<String> limitException(LimitException limitException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(limitException.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> notFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
    }
}
