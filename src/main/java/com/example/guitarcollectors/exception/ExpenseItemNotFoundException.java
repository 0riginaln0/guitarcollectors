package com.example.guitarcollectors.exception;

public class ExpenseItemNotFoundException extends RuntimeException {

    public ExpenseItemNotFoundException(Long id) {
        super(("Could not find expense item " + id));
    }

}
