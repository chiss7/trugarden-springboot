package com.chis.trugarden.shared.exception;

public class UserAlreadyEnabledException extends RuntimeException {
    public UserAlreadyEnabledException(String msg) {
        super(msg);
    }
}
