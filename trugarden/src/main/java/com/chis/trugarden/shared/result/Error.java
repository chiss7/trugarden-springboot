package com.chis.trugarden.shared.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {

    public static final Error NONE = new Error("", "", ErrorType.FAILURE);
    public static final Error NULL_VALUE = new Error("General.Null", "Null value was provided", ErrorType.FAILURE);

    private final String code;
    private final String description;
    private final ErrorType type;

    public static Error failure(String code, String description) {
        return new Error(code, description, ErrorType.FAILURE);
    }

    public static Error notFound(String code, String description) {
        return new Error(code, description, ErrorType.NOT_FOUND);
    }

    public static Error problem(String code, String description) {
        return new Error(code, description, ErrorType.PROBLEM);
    }

    public static Error conflict(String code, String description) {
        return new Error(code, description, ErrorType.CONFLICT);
    }
}
