package com.chis.trugarden.shared.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String msg) {
        super(msg);
    }
}
