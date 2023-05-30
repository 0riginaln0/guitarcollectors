package com.example.guitarcollectors.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.guitarcollectors.exception.ExpenseItemHasChargesException;
import com.example.guitarcollectors.exception.ExpenseItemNotFoundException;

@ControllerAdvice
public class ServiceLayerAdvice {
    @ResponseBody
    @ExceptionHandler(ExpenseItemHasChargesException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String expenseItemHasChargesHandler(ExpenseItemHasChargesException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ExpenseItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String expenseItemNotFoundHandler(ExpenseItemNotFoundException ex) {
        return ex.getMessage();
    }
}
