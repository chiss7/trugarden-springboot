package com.chis.trugarden.application.cart.get;

import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.shared.enums.CartStatus;

import java.math.BigDecimal;
import java.util.List;

public record GetUserCartResult(
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
        CartStatus status,
        List<CartItemResult> cartItems,
        boolean isAuthenticated
) {
    public static GetUserCartResult from(Cart cart, boolean isAuthenticated) {
        List<CartItemResult> items = cart.getCartItems().stream()
                .map(CartItemResult::from)
                .toList();

        return new GetUserCartResult(
                cart.getId(),
                cart.getSessionId(),
                cart.getSubtotal(),
                cart.getTotalTax(),
                cart.getTotalWithTax(),
                cart.getDiscountPercentage(),
                cart.getCouponDiscountAmount(),
                cart.getGrandTotal(),
                cart.getQuantity(),
                cart.getCouponCode(),
                cart.getStatus(),
                items,
                isAuthenticated
        );
    }

    public record CartItemResult(
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
        public static CartItemResult from(CartItem item) {
            Product product = item.getProduct();
            String firstImage = product.getImages().isEmpty() ? null : product.getImages().getFirst();

            return new CartItemResult(
                    item.getId(),
                    product.getId(),
                    product.getName(),
                    product.getSlug(),
                    firstImage,
                    item.getQuantity(),
                    item.getOriginalPrice() != null ? item.getOriginalPrice() : BigDecimal.ZERO,
                    item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO,
                    product.getUnitPriceWithTax(),
                    item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO,
                    item.getTotalWithTax(),
                    item.getTaxPercentage(),
                    item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO,
                    product.getDiscountPercentage(),
                    product.getStock()
            );
        }
    }
}
