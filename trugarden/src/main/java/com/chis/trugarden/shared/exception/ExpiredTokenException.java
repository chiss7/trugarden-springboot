package com.chis.trugarden.shared.exception;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String msg) {
        super(msg);
    }
}
