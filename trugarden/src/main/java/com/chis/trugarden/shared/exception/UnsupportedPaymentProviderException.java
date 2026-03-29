package com.chis.trugarden.shared.exception;

public class UnsupportedPaymentProviderException extends RuntimeException {
    public UnsupportedPaymentProviderException(String msg) {
        super(msg);
    }
}
