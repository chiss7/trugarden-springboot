package com.chis.trugarden.domain.role;

import com.chis.trugarden.shared.enums.Roles;
import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleErrors {
    public static Error notFound(Roles name) {
        return Error.notFound(
                "ROLE_NOT_FOUND",
                String.format("No se encontró el rol '%s'.", name)
        );
    }
}
