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
}
