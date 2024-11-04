package com.chis.trugarden.exception;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String msg) {
        super(msg);
    }
}
