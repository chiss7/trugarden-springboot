package com.chis.trugarden.domain.product;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductErrors {
    public static Error notFound(Long id) {
        return Error.notFound(
                "PRODUCT_NOT_FOUND",
                String.format("No se encontró el producto '%s'.", id)
        );
    }

    public static Error slugAlreadyExists(String slug) {
        return Error.conflict(
                "PRODUCT_SLUG_ALREADY_EXISTS",
                String.format("El slug '%s' ya está en uso por otro producto.", slug)
        );
    }
}
