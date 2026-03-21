package com.chis.trugarden.shared.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {
    NO_CODE("NO_CODE", HttpStatus.NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD("INCORRECT_CURRENT_PASSWORD", HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta"),
    PASSWORDS_DO_NOT_MATCH("PASSWORDS_DO_NOT_MATCH", HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden"),
    ACCOUNT_LOCKED("ACCOUNT_LOCKED", HttpStatus.FORBIDDEN, "La cuenta del usuario está bloqueada. Por favor, inténtalo de nuevo más tarde."),
    ACCOUNT_DISABLED("ACCOUNT_DISABLED", HttpStatus.FORBIDDEN, "La cuenta del usuario está deshabilitada. Por favor, contacta al soporte para más información."),
    BAD_CREDENTIALS("BAD_CREDENTIALS", HttpStatus.FORBIDDEN, "Correo electrónico o contraseña incorrectos"),
    INVALID_TOKEN("INVALID_TOKEN", HttpStatus.BAD_REQUEST, "Token inválido"),
    EXPIRED_TOKEN("EXPIRED_TOKEN", HttpStatus.BAD_REQUEST, "Token expirado"),
    ACCOUNT_ALREADY_ENABLED("ACCOUNT_ALREADY_ENABLED", HttpStatus.BAD_REQUEST, "Cuenta ya habilitada"),
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.UNPROCESSABLE_ENTITY, "Error de validación en los datos proporcionados"),
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
