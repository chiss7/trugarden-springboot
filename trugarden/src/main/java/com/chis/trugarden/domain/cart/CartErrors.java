package com.chis.trugarden.domain.cart;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

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

    public static Error cartNotActive() {
        return Error.conflict(
                "CART_NOT_ACTIVE",
                "Tu carrito no está activo. Por favor, crea un nuevo carrito para continuar."
        );
    }

    public static Error invalidCart() {
        return Error.conflict(
                "INVALID_CART",
                "Algunos productos en tu carrito han cambiado. Por favor revísalo antes de continuar."
        );
    }

    public static Error emptyCart() {
        return Error.conflict(
                "EMPTY_CART",
                "Tu carrito está vacío. Agrega algunos productos antes de continuar."
        );
    }

    public static Error itemsOutOfStock(Set<CartItem> items) {
        String productsMessage = items.stream()
                .map(item -> item.getProduct().getName() +
                        " (disponible: " + item.getProduct().getStock() + ")")
                .collect(Collectors.joining(", "));

        return Error.conflict(
                "ITEMS_OUT_OF_STOCK",
                "Los siguientes productos no tienen stock suficiente: " + productsMessage
        );
    }

    public static Error madeToOrderLimitExceeded(int maxAllowed) {
        return Error.conflict(
                "MADE_TO_ORDER_LIMIT_EXCEEDED",
                "La cantidad solicitada supera el máximo permitido para productos bajo pedido (máximo " +
                        maxAllowed + ")."
        );
    }

    public static Error sessionMismatch() {
        return Error.conflict(
                "SESSION_MISMATCH",
                "El ID de sesión proporcionado no coincide con el del carrito."
        );
    }
}
