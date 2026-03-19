package com.chis.trugarden.shared.api;

import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

public abstract class ControllerBase {
    /**
     * Success response
     */
    protected <T> ResponseEntity<GenericResponse<T>> success(T data) {
        return ResponseEntity.ok(GenericResponse.createSuccessResponse(data));
    }

    /**
     * Success response with messages
     */
    protected <T> ResponseEntity<GenericResponse<T>> success(T data, List<String> messages) {
        return ResponseEntity.ok(GenericResponse.createSuccessResponse(data, messages));
    }

    /**
     * Success response with no data
     */
    protected ResponseEntity<GenericResponse<String>> success() {
        return ResponseEntity.ok(GenericResponse.createSuccessResponse());
    }

    /**
     * Error response mapped from a Result Error.
     * Maps ErrorType to the appropriate HTTP status code.
     */
    protected ResponseEntity<GenericResponse<?>> error(Error error) {
        HttpStatus status = mapErrorTypeToHttpStatus(error.getType());
        return ResponseEntity.status(status).body(GenericResponse.createErrorResponse(error.getCode(), error.getDescription()));
    }

    /**
     * Created response with location
     */
    protected <T> ResponseEntity<GenericResponse<?>> created(String newId, T entity) {
        URI location = URI.create("/" + newId);
        return ResponseEntity.created(location).body(GenericResponse.createSuccessResponse(entity));
    }

    private HttpStatus mapErrorTypeToHttpStatus(ErrorType errorType) {
        return switch (errorType) {
            case VALIDATION -> HttpStatus.UNPROCESSABLE_ENTITY;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case PROBLEM -> HttpStatus.BAD_REQUEST;
            case FAILURE -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
