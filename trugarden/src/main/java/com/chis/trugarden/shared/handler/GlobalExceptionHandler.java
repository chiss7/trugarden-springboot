package com.chis.trugarden.shared.handler;

import com.chis.trugarden.shared.api.GenericResponse;
import com.chis.trugarden.shared.exception.ExpiredTokenException;
import com.chis.trugarden.shared.exception.InvalidTokenException;
import com.chis.trugarden.shared.exception.PasswordMismatchException;
import com.chis.trugarden.shared.exception.UserAlreadyEnabledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<GenericResponse<?>> handleException(LockedException exp) {
        return ResponseEntity.status(BusinessErrorCodes.ACCOUNT_LOCKED.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.ACCOUNT_LOCKED.getCode(),
                        BusinessErrorCodes.ACCOUNT_LOCKED.getDescription()
                ));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<GenericResponse<?>> handleException(DisabledException exp) {
        return ResponseEntity.status(BusinessErrorCodes.ACCOUNT_DISABLED.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.ACCOUNT_DISABLED.getCode(),
                        BusinessErrorCodes.ACCOUNT_DISABLED.getDescription()
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericResponse<?>> handleException(BadCredentialsException exp) {
        return ResponseEntity.status(BusinessErrorCodes.BAD_CREDENTIALS.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.BAD_CREDENTIALS.getCode(),
                        BusinessErrorCodes.BAD_CREDENTIALS.getDescription()
                ));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<GenericResponse<?>> handleException(PasswordMismatchException exp) {
        return ResponseEntity.status(BusinessErrorCodes.PASSWORDS_DO_NOT_MATCH.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.PASSWORDS_DO_NOT_MATCH.getCode(),
                        BusinessErrorCodes.PASSWORDS_DO_NOT_MATCH.getDescription()
                ));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<GenericResponse<?>> handleException(InvalidTokenException exp) {
        return ResponseEntity.status(BusinessErrorCodes.INVALID_TOKEN.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.INVALID_TOKEN.getCode(),
                        BusinessErrorCodes.INVALID_TOKEN.getDescription()
                ));
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<GenericResponse<?>> handleException(ExpiredTokenException exp) {
        return ResponseEntity.status(BusinessErrorCodes.EXPIRED_TOKEN.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.EXPIRED_TOKEN.getCode(),
                        BusinessErrorCodes.EXPIRED_TOKEN.getDescription()
                ));
    }

    @ExceptionHandler(UserAlreadyEnabledException.class)
    public ResponseEntity<GenericResponse<?>> handleException(UserAlreadyEnabledException exp) {
        return ResponseEntity.status(BusinessErrorCodes.ACCOUNT_ALREADY_ENABLED.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.ACCOUNT_ALREADY_ENABLED.getCode(),
                        BusinessErrorCodes.ACCOUNT_ALREADY_ENABLED.getDescription()
                ));
    }

    /**
     * Handles validation errors triggered by @Valid on @RequestBody.
     * Collects all field-level error messages and returns them as a list.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        log.warn("Validation failed: {}", errors);

        return ResponseEntity
                .status(BusinessErrorCodes.VALIDATION_ERROR.getHttpStatus())
                .body(GenericResponse.createErrorResponse(BusinessErrorCodes.VALIDATION_ERROR.getCode(), errors));
    }

    /**
     * Handles malformed JSON or missing request body.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericResponse<?>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed request body: {}", ex.getMessage());

        return ResponseEntity
                .status(BusinessErrorCodes.MALFORMED_REQUEST.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.MALFORMED_REQUEST.getCode(),
                        BusinessErrorCodes.MALFORMED_REQUEST.getDescription()
                ));
    }

    /**
     * Handles unsupported HTTP methods (e.g. GET on a POST-only endpoint).
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GenericResponse<?>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = String.format("HTTP method '%s' is not supported for this endpoint.", ex.getMethod());
        log.warn(message);

        return ResponseEntity
                .status(BusinessErrorCodes.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(GenericResponse.createErrorResponse(BusinessErrorCodes.METHOD_NOT_ALLOWED.getCode(), message));
    }

    /**
     * Handles requests to non-existent resources/routes.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());

        return ResponseEntity
                .status(BusinessErrorCodes.RESOURCE_NOT_FOUND.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.RESOURCE_NOT_FOUND.getCode(),
                        BusinessErrorCodes.RESOURCE_NOT_FOUND.getDescription()
                ));
    }

    /**
     * Catch-all handler for any unhandled exceptions.
     * Logs the full stack trace but returns a generic message to avoid leaking internals.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<?>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        return ResponseEntity
                .status(BusinessErrorCodes.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(GenericResponse.createErrorResponse(
                        BusinessErrorCodes.INTERNAL_SERVER_ERROR.getCode(),
                        BusinessErrorCodes.INTERNAL_SERVER_ERROR.getDescription()
                ));
    }
}
