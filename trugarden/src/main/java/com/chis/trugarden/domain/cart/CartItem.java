package com.chis.trugarden.domain.cart;

import com.chis.trugarden.domain.product.Product;

import java.math.BigDecimal;

public class CartItem {
    private final Long id;
    private final Long cartId;
    private final Product product;
    private final int quantity;
    private final BigDecimal mrpPrice;
    private final BigDecimal sellingPrice;
    private final Long userId;

    public CartItem(
            Long id,
            Long cartId,
            Product product,
            int quantity,
            BigDecimal mrpPrice,
            BigDecimal sellingPrice,
            Long userId
    ) {
        this.id = id;
        this.cartId = cartId;
        this.product = product;
        this.quantity = quantity;
        this.mrpPrice = mrpPrice;
        this.sellingPrice = sellingPrice;
        this.userId = userId;
    }

    public static CartItem of(
            Long id,
            Long cartId,
            Product product,
            int quantity,
            BigDecimal mrpPrice,
            BigDecimal sellingPrice,
            Long userId
    ) {
        return new CartItem(id, cartId, product, quantity, mrpPrice, sellingPrice, userId);
    }

    public static CartItem ofNew(
            Long cartId,
            Product product,
            int quantity,
            BigDecimal mrpPrice,
            BigDecimal sellingPrice,
            Long userId
    ) {
        return new CartItem(null, cartId, product, quantity, mrpPrice, sellingPrice, userId);
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

    public BigDecimal getMrpPrice() {
        return mrpPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", product=" + product +
                ", quantity=" + quantity +
                ", mrpPrice=" + mrpPrice +
                ", sellingPrice=" + sellingPrice +
                ", userId=" + userId +
                '}';
    }
}
