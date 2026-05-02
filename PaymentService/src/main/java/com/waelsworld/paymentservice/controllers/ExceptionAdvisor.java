package com.waelsworld.paymentservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvisor {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // here exception e can be logged or any corrective action can be taken
        //  for now just printing the stack trace
        return new ResponseEntity<>("Oops!! Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
