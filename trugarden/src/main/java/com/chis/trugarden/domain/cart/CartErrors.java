package com.chis.trugarden.domain.cart;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CartErrors {
    public static Error notFound(String sessionId) {
        return Error.notFound(
                "CART_NOT_FOUND",
                String.format("No se encontró el carrito para la sesión '%s'.", sessionId)
        );
    }

    public static Error notFound() {
        return Error.notFound(
                "CART_NOT_FOUND",
                "No se encontró el carrito para este usuario."
        );
    }

    public static Error invalidAction() {
        return Error.conflict(
                "INVALID_ACTION",
                "El usuario no está autenticado o no se proporcionó un ID de sesión válido."
        );
    }

    public static Error unableToUpdateCart() {
        return Error.conflict(
                "UNABLE_TO_UPDATE_CART",
                "No se puede actualizar el carrito por su estado actual."
        );
    }

    public static Error outOfStock() {
        return Error.conflict(
                "OUT_OF_STOCK",
                "Lo sentimos, no puedes agregar más artículos al carrito porque el producto está agotado."
        );
    }

    public static Error itemNotInCart(Long productId) {
        return Error.notFound(
                "ITEM_NOT_IN_CART",
                String.format("El producto con ID '%d' no se encuentra en el carrito.", productId)
        );
    }
}
