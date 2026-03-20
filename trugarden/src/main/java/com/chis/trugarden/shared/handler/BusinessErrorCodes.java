package com.chis.trugarden.shared.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {
    NO_CODE("NO_CODE", HttpStatus.NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD("INCORRECT_CURRENT_PASSWORD", HttpStatus.BAD_REQUEST, "Current password is incorrect"),
    PASSWORDS_DO_NOT_MATCH("PASSWORDS_DO_NOT_MATCH", HttpStatus.BAD_REQUEST, "Passwords do not match"),
    ACCOUNT_LOCKED("ACCOUNT_LOCKED", HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED("ACCOUNT_DISABLED", HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS("BAD_CREDENTIALS", HttpStatus.FORBIDDEN, "Email and/or password is incorrect"),
    INVALID_TOKEN("INVALID_TOKEN", HttpStatus.BAD_REQUEST, "Invalid token"),
    EXPIRED_TOKEN("EXPIRED_TOKEN", HttpStatus.BAD_REQUEST, "Expired token"),
    ACCOUNT_ALREADY_ENABLED("ACCOUNT_ALREADY_ENABLED", HttpStatus.BAD_REQUEST, "Account already enabled"),
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.UNPROCESSABLE_ENTITY, "Validation error"),
    MALFORMED_REQUEST("MALFORMED_REQUEST", HttpStatus.BAD_REQUEST, "Malformed or missing request body"),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not allowed"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, "The requested resource was not found"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado. Inténtalo de nuevo más tarde")
    ;
    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(String code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
