package com.chis.trugarden.domain.order;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderErrors {
    public static Error notFound() {
        return Error.notFound(
                "ORDER_NOT_FOUND",
                "No se encontró la orden especificada."
        );
    }

    public static Error notFound(String orderId) {
        return Error.notFound(
                "ORDER_NOT_FOUND",
                String.format("No se encontró la orden '%s'.", orderId)
        );
    }

    public static Error invalidStatus() {
        return Error.conflict(
                "ORDER_INVALID_STATUS",
                "El estado del pago no es válido."
        );
    }
}
