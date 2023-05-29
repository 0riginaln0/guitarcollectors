package com.example.guitarcollectors.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ChargeNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ChargeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String chargeNotFoundHandler(ChargeNotFoundException ex) {
        return ex.getMessage();
    }
}
