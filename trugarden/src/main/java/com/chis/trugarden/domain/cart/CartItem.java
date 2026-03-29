package com.chis.trugarden.domain.cart;

import com.chis.trugarden.domain.product.Product;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
public class CartItem {
    private final Long id;
    private final Long cartId;
    private final Product product;
    private final int quantity;
    private final BigDecimal originalPrice;
    private final BigDecimal unitPrice;
    private final BigDecimal subtotal;
    private final int taxPercentage;
    private final BigDecimal taxAmount;
    private final Long userId;

    public CartItem(
            Long id,
            Long cartId,
            Product product,
            int quantity,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            int taxPercentage,
            Long userId
    ) {
        this.id = id;
        this.cartId = cartId;
        this.product = Objects.requireNonNull(product, "product cannot be null");
        this.quantity = quantity;
        this.originalPrice = Objects.requireNonNull(originalPrice, "originalPrice cannot be null");
        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice cannot be null");
        this.taxPercentage = taxPercentage;
        this.subtotal = calculateSubtotal();
        this.taxAmount = calculateTaxAmount();
        this.userId = userId;
    }

    public static CartItem of(
            Long id,
            Long cartId,
            Product product,
            int quantity,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            int taxPercentage,
            Long userId
    ) {
        return new CartItem(id, cartId, product, quantity, originalPrice, unitPrice, taxPercentage, userId);
    }

    public static CartItem ofNew(
            Long cartId,
            Product product,
            int quantity,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            int taxPercentage,
            Long userId
    ) {
        return new CartItem(null, cartId, product, quantity, originalPrice, unitPrice, taxPercentage, userId);
    }

    /**
     * Creates a CartItem from a Product, extracting price and tax information automatically.
     */
    public static CartItem fromProduct(
            Long id,
            Long cartId,
            Product product,
            int quantity,
            Long userId
    ) {
        return new CartItem(
                id,
                cartId,
                product,
                quantity,
                product.getOriginalPrice(),
                product.getUnitPrice(),
                product.isHasIva() ? product.getIvaPercentage() : 0,
                userId
        );
    }

    /**
     * Creates a new CartItem from a Product (without id).
     */
    public static CartItem newFromProduct(
            Long cartId,
            Product product,
            int quantity,
            Long userId
    ) {
        return fromProduct(null, cartId, product, quantity, userId);
    }

    public Long getId() {
        return id;
    }

    public Long getCartId() {
        return cartId;
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

    /**
     * Calculates subtotal: unitPrice × quantity
     */
    private BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Calculates the tax amount to add to the subtotal.
     * Since unitPrice is the base price (without IVA): taxAmount = subtotal * (taxPercentage/100)
     */
    private BigDecimal calculateTaxAmount() {
        if (taxPercentage == 0) {
            return BigDecimal.ZERO;
        }
        return subtotal.multiply(
                BigDecimal.valueOf(taxPercentage).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        ).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns the total amount including tax (subtotal + taxAmount).
     */
    public BigDecimal getTotalWithTax() {
        return subtotal.add(taxAmount);
    }

    /**
     * Creates a new CartItem with updated quantity.
     */
    public CartItem withQuantity(int newQuantity) {
        return new CartItem(id, cartId, product, newQuantity, originalPrice, unitPrice, taxPercentage, userId);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", cartId=" + cartId +
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
