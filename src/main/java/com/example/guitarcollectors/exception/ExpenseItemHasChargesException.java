package com.example.guitarcollectors.exception;

public class ExpenseItemHasChargesException extends RuntimeException {
    public ExpenseItemHasChargesException(Long id) {
        super(("Cannot delete expense item with id " + id + " because there are charges using it"));
    }
}
