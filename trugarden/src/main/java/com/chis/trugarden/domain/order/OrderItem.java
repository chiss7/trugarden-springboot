package com.chis.trugarden.domain.order;

import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.domain.product.Product;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class OrderItem {
    private final Long id;
    private final Long orderId;
    private final Product product;
    private final int quantity;
    private final BigDecimal originalPrice;
    private final BigDecimal unitPrice;
    private final BigDecimal subtotal;
    private final int taxPercentage;
    private final BigDecimal taxAmount;
    private final Long userId;

    public OrderItem(
            Long id,
            Long orderId,
            Product product,
            int quantity,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            BigDecimal subtotal,
            int taxPercentage,
            BigDecimal taxAmount,
            Long userId
    ) {
        this.id = id;
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.taxPercentage = taxPercentage;
        this.taxAmount = taxAmount;
        this.userId = userId;
    }

    public static OrderItem of(
            Long id,
            Long orderId,
            Product product,
            int quantity,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            BigDecimal subtotal,
            int taxPercentage,
            BigDecimal taxAmount,
            Long userId
    ) {
        return new OrderItem(
                id, orderId, product, quantity, originalPrice, unitPrice, subtotal, taxPercentage, taxAmount, userId
        );
    }

    public static OrderItem ofNew(
            Long orderId,
            Product product,
            int quantity,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            BigDecimal subtotal,
            int taxPercentage,
            BigDecimal taxAmount,
            Long userId
    ) {
        return new OrderItem(
                null, orderId, product, quantity, originalPrice, unitPrice, subtotal, taxPercentage, taxAmount, userId
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public int getTaxPercentage() {
        return taxPercentage;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public static OrderItem fromCartItem(CartItem cartItem, Long orderId) {
        BigDecimal subtotal = cartItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        BigDecimal taxAmount = subtotal
                .multiply(BigDecimal.valueOf(cartItem.getTaxPercentage()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return OrderItem.ofNew(
                orderId,
                cartItem.getProduct(),
                cartItem.getQuantity(),
                cartItem.getOriginalPrice(),
                cartItem.getUnitPrice(),
                subtotal,
                cartItem.getTaxPercentage(),
                taxAmount,
                cartItem.getUserId()
        );
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", product=" + product +
                ", quantity=" + quantity +
                ", originalPrice=" + originalPrice +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", taxPercentage=" + taxPercentage +
                ", taxAmount=" + taxAmount +
                ", userId=" + userId +
                '}';
    }
}
