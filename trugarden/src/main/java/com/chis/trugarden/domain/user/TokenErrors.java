package com.chis.trugarden.domain.user;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenErrors {
    public static Error notFound(String token) {
        return Error.notFound(
                "TOKEN_NOT_FOUND",
                String.format("No se encontró un token '%s'. Proporciona uno nuevo.", token)
        );
    }

    public static Error notValid(String token) {
        return Error.conflict(
                "TOKEN_INVALID",
                String.format("El token '%s' no es válido.", token)
        );
    }
}
