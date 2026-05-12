package com.chis.trugarden.application.cart.update_cart_item;

import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.shared.enums.CartStatus;

import java.math.BigDecimal;
import java.util.List;

public record UpdateCartItemResult(
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
        int etaMinDays,
        int etaMaxDays,
        List<CartItemResult> cartItems,
        boolean isAuthenticated
) {
    public static UpdateCartItemResult from(Cart cart, boolean isAuthenticated) {
        if (cart == null) {
            return new UpdateCartItemResult(
                    null,
                    null,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    0,
                    null,
                    CartStatus.NOT_CREATED,
                    0,
                    0,
                    List.of(),
                    isAuthenticated
            );
        }

        List<CartItemResult> items = cart.getCartItems().stream()
                .map(CartItemResult::from)
                .toList();

        int etaMinDays = calculateEtaMinDays(items);
        int etaMaxDays = calculateEtaMaxDays(items);

        return new UpdateCartItemResult(
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
                etaMinDays,
                etaMaxDays,
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
            double stock,
            int leadTimeMinDays,
            int leadTimeMaxDays,
            String productType
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
                    product.getStock(),
                    item.getLeadTimeMinDays(),
                    item.getLeadTimeMaxDays(),
                    product.getProductType().name()
            );
        }
    }

    private static int calculateEtaMinDays(List<CartItemResult> items) {
        return items.stream()
                .mapToInt(CartItemResult::leadTimeMinDays)
                .max()
                .orElse(0);
    }

    private static int calculateEtaMaxDays(List<CartItemResult> items) {
        return items.stream()
                .mapToInt(CartItemResult::leadTimeMaxDays)
                .max()
                .orElse(0);
    }
}
