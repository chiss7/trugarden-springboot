package com.chis.trugarden.domain.auth;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RefreshTokenErrors {
    public static Error missingCookie() {
        return Error.problem(
                "REFRESH_TOKEN_MISSING",
                "No se encontro el refresh token en cookies."
        );
    }

    public static Error invalidToken() {
        return Error.notFound(
                "REFRESH_TOKEN_INVALID",
                "El refresh token no es valido o ya fue revocado."
        );
    }

    public static Error expiredToken() {
        return Error.conflict(
                "REFRESH_TOKEN_EXPIRED",
                "El refresh token ha expirado."
        );
    }
}
