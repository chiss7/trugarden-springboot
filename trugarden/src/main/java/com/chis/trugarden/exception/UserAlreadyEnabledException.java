package com.chis.trugarden.exception;

public class UserAlreadyEnabledException extends RuntimeException {
    public UserAlreadyEnabledException(String msg) {
        super(msg);
    }
}
