package com.example.guitarcollectors.exception;

public class ChargeNotFoundException extends RuntimeException {

    public ChargeNotFoundException(Long id) {
        super(("Could not find charge " + id));
    }
}
