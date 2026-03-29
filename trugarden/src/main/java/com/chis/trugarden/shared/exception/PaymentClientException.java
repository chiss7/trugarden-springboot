package com.chis.trugarden.shared.exception;

public class PaymentClientException extends RuntimeException {
    public PaymentClientException(String msg) {
        super(msg);
    }

    public PaymentClientException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
