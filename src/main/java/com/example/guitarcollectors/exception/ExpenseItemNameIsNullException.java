package com.example.guitarcollectors.exception;

public class ExpenseItemNameIsNullException extends RuntimeException {
    public ExpenseItemNameIsNullException(String message) {
        super((message));
    }
}
