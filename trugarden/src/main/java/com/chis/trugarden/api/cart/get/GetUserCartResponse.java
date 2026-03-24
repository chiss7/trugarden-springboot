package com.chis.trugarden.api.cart.get;

import java.math.BigDecimal;
import java.util.List;

public record GetUserCartResponse(
        Long cartId,
        String sessionId,
        BigDecimal subtotal,
        BigDecimal totalTax,
        BigDecimal subtotalWithTax,
        BigDecimal discountPercentage,
        BigDecimal couponDiscountAmount,
        BigDecimal grandTotal,
        int quantity,
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
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            BigDecimal unitPriceWithTax,
            BigDecimal subtotal,
            BigDecimal subtotalWithTax,
            int taxPercentage,
            BigDecimal taxAmount,
            double discountPercentage,
            double stock
    ) {
    }
}
