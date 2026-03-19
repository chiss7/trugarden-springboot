package com.chis.trugarden.shared.api;

import lombok.Data;

import java.util.List;

@Data
public class GenericResponse<T> {
    private String status;
    private String code;
    private T data;
    private List<String> messages;

    private GenericResponse(String status, String code, T data, List<String> messages) {
        this.status = status;
        this.code = code;
        this.data = data;
        this.messages = messages;
    }

    public static <T> GenericResponse<T> createSuccessResponse(T data) {
        return new GenericResponse<>("OK", null, data, List.of());
    }

    public static <T> GenericResponse<T> createSuccessResponse(T data, List<String> messages) {
        return new GenericResponse<>("OK", null, data, messages);
    }

    public static <T> GenericResponse<T> createSuccessResponse() {
        return new GenericResponse<>("OK", null, null, List.of());
    }

    public static <T> GenericResponse<T> createErrorResponse(String code, String errorMessage) {
        return new GenericResponse<>("ERROR", code, null, List.of(errorMessage));
    }

    public static <T> GenericResponse<T> createErrorResponse(String code, List<String> errorMessages) {
        return new GenericResponse<>("ERROR", code, null, errorMessages);
    }
}
