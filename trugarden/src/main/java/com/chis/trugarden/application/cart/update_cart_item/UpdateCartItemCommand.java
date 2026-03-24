package com.chis.trugarden.application.cart.update_cart_item;

public record UpdateCartItemCommand(
        String sessionId,
        Long productId,
        int quantity
) {
}
