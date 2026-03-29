package com.chis.trugarden.shared.exception;

public class PaymentProviderException extends RuntimeException {
    public PaymentProviderException(String msg) {
        super(msg);
    }

    public PaymentProviderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
