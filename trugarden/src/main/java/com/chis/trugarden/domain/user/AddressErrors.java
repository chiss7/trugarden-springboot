package com.chis.trugarden.domain.user;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AddressErrors {
    public static Error notFound() {
        return Error.notFound(
                "ADDRESS_NOT_FOUND",
                "No se encontró la dirección proporcionada."
        );
    }
}
