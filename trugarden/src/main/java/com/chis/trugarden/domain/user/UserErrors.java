package com.chis.trugarden.domain.user;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserErrors {
    public static Error alreadyExists(String email) {
        return Error.problem(
                "USER_ALREADY_EXISTS",
                String.format("Ya existe un usuario con el email '%s'.", email)
        );
    }

    public static Error notFound(String email) {
        return Error.notFound(
                "USER_NOT_FOUND",
                String.format("No se encontró un usuario con el email '%s'.", email)
        );
    }

    public static Error alreadyEnabled(String email) {
        return Error.conflict(
                "USER_ALREADY_ENABLED",
                String.format("El usuario con email '%s' ya está habilitado.", email)
        );
    }

    public static Error notAuthenticated() {
        return Error.conflict(
                "USER_NOT_AUTHENTICATED",
                "El usuario no está autenticado."
        );
    }
}
