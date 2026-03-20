package com.chis.trugarden.domain.category;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryErrors {
    public static Error notFound(String code) {
        return Error.notFound(
                "CATEGORY_NOT_FOUND",
                String.format("No se encontró la categoría '%s'.", code)
        );
    }
}
