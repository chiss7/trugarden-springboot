package com.chis.trugarden.api.cart.get;

import java.math.BigDecimal;
import java.util.List;

public record GetUserCartResponse(
        Long cartId,
        String sessionId,
        BigDecimal totalPrice,
        BigDecimal totalMrpPrice,
        int quantity,
        int discount,
        String couponCode,
        List<CartItemResponse> cartItems,
        boolean isAuthenticated
) {
    public record CartItemResponse(
            Long id,
            Long productId,
            String productName,
            String productSlug,
            String productImage,
            int quantity,
            BigDecimal mrpPrice,
            BigDecimal sellingPrice,
            double discountPercentage,
            double stock
    ) {
    }
}
