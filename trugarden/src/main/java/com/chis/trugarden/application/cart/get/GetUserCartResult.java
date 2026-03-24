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
        BigDecimal totalPrice,
        BigDecimal totalMrpPrice,
        int quantity,
        int discount,
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
                cart.getTotalPrice(),
                cart.getTotalMrpPrice(),
                cart.getQuantity(),
                cart.getDiscount(),
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
            BigDecimal mrpPrice,
            BigDecimal sellingPrice,
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
                    item.getMrpPrice() != null ? item.getMrpPrice() : BigDecimal.ZERO,
                    item.getSellingPrice() != null ? item.getSellingPrice() : BigDecimal.ZERO,
                    product.getDiscountPercentage(),
                    product.getStock()
            );
        }
    }
}
