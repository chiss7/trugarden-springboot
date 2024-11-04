package com.chis.trugarden.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Current password is incorrect"),
    PASSWORDS_DO_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "Passwords do not match"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, HttpStatus.FORBIDDEN, "Email and/or password is incorrect"),
    INVALID_TOKEN(305, HttpStatus.BAD_REQUEST, "Invalid token"),
    EXPIRED_TOKEN(306, HttpStatus.BAD_REQUEST, "Expired token"),
    ACCOUNT_ALREADY_ENABLED(307, HttpStatus.BAD_REQUEST, "Account already enabled")
    ;
    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
