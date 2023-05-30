package com.example.guitarcollectors.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.guitarcollectors.exception.ChargeNotFoundException;
import com.example.guitarcollectors.exception.ExpenseItemNameIsEmptyException;
import com.example.guitarcollectors.exception.ExpenseItemNameIsNullException;

@ControllerAdvice
public class ControllerLayerAdvice {
    @ResponseBody
    @ExceptionHandler(ChargeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String chargeNotFoundHandler(ChargeNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ExpenseItemNameIsEmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String expenseItemNameIsEmptyHandler(ExpenseItemNameIsEmptyException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ExpenseItemNameIsNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String expenseItemNameIsNullHandler(ExpenseItemNameIsNullException ex) {
        return ex.getMessage();
    }
}
