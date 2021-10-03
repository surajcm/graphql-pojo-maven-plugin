package com.github.surajcm.exception;

public class ValidationException extends Exception {

    private static final long serialVersionUID = 42L;

    public ValidationException(final String errorMessage) {
        super(errorMessage);
    }
}