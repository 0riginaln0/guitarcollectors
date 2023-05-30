package com.example.guitarcollectors.exception;

public class ExpenseItemNameIsEmptyException extends RuntimeException {
    public ExpenseItemNameIsEmptyException(String message) {
        super((message));
    }

}
