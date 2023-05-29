package com.example.guitarcollectors.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExpenseItemNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ExpenseItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String chargeNotFoundHandler(ExpenseItemNotFoundException ex) {
        return ex.getMessage();
    }
}
