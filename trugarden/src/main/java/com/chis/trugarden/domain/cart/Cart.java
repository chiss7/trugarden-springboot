package com.chis.trugarden.domain.cart;

import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.shared.enums.CartStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Cart {
    private final Long id;
    private final User user;
    private final String sessionId;
    private final BigDecimal totalPrice;
    private final BigDecimal totalMrpPrice;
    private final int quantity;
    private final int discount;
    private final String couponCode;
    private final CartStatus status;
    private final Set<CartItem> cartItems;

    public Cart(
            Long id,
            User user,
            String sessionId,
            BigDecimal totalPrice,
            BigDecimal totalMrpPrice,
            int quantity,
            int discount,
            String couponCode,
            CartStatus status,
            Set<CartItem> cartItems
    ) {
        this.id = id;
        this.user = user;
        this.sessionId = sessionId;
        this.totalPrice = Objects.requireNonNull(totalPrice, "totalPrice cannot be null");
        this.totalMrpPrice = Objects.requireNonNull(totalMrpPrice, "totalMrpPrice cannot be null");
        this.quantity = quantity;
        this.discount = discount;
        this.couponCode = couponCode;
        this.status = status;
        this.cartItems = cartItems;
    }

    public static Cart of(
            Long id,
            User user,
            String sessionId,
            BigDecimal totalPrice,
            BigDecimal totalMrpPrice,
            int quantity,
            int discount,
            String couponCode,
            CartStatus status,
            Set<CartItem> cartItems
    ) {
        return new Cart(
                id,
                user,
                sessionId,
                totalPrice,
                totalMrpPrice,
                quantity,
                discount,
                couponCode,
                status,
                cartItems
        );
    }

    public static Cart ofNew(
            User user,
            String sessionId,
            BigDecimal totalPrice,
            BigDecimal totalMrpPrice,
            int quantity,
            int discount,
            String couponCode,
            CartStatus status,
            Set<CartItem> cartItems
    ) {
        return new Cart(
                null,
                user,
                sessionId,
                totalPrice,
                totalMrpPrice,
                quantity,
                discount,
                couponCode,
                status,
                cartItems
        );
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getTotalMrpPrice() {
        return totalMrpPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscount() {
        return discount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public CartStatus getStatus() {
        return status;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public Cart addItem(CartItem item) {
        Set<CartItem> newItems = new HashSet<>(cartItems);
        newItems.add(item);
        return withCartItems(newItems);
    }

    public Cart removeItem(Long productId) {
        Set<CartItem> newItems = cartItems.stream()
                .filter(item -> !item.getProduct().getId().equals(productId))
                .collect(Collectors.toSet());
        return withCartItems(newItems);
    }

    public Cart updateItemQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            return removeItem(productId);
        }
        Set<CartItem> newItems = cartItems.stream()
                .map(item -> item.getProduct().getId().equals(productId)
                        ? CartItem.of(
                            item.getId(),
                            item.getCartId(),
                            item.getProduct(),
                            quantity,
                            item.getMrpPrice(),
                            item.getSellingPrice(),
                            item.getUserId())
                        : item)
                .collect(Collectors.toSet());
        return withCartItems(newItems);
    }

    public Cart clear() {
        return withCartItems(new HashSet<>());
    }

    public BigDecimal getTotalSellingPrice() {
        return cartItems.stream()
                .map(item -> item.getSellingPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getCalculatedTotalMrpPrice() {
        return cartItems.stream()
                .map(item -> item.getMrpPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalQuantity() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public int getTotalDiscount() {
        return getCalculatedTotalMrpPrice().subtract(getTotalSellingPrice()).intValue();
    }

    public Cart applyCoupon(String code) {
        return new Cart(
                id, user, sessionId, totalPrice, totalMrpPrice,
                quantity, discount, Objects.requireNonNull(code),
                status, cartItems
        );
    }

    public Cart removeCoupon() {
        return new Cart(
                id, user, sessionId, totalPrice, totalMrpPrice,
                quantity, discount, null, status, cartItems
        );
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public boolean isActive() {
        return status == CartStatus.ACTIVE;
    }

    public boolean isNotCreated() {
        return status == CartStatus.NOT_CREATED;
    }

    public boolean hasItem(Long productId) {
        return cartItems.stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId));
    }

    public boolean isValid() {
        if (isEmpty()) {
            return totalPrice.compareTo(BigDecimal.ZERO) == 0
                    && totalMrpPrice.compareTo(BigDecimal.ZERO) == 0
                    && quantity == 0;
        }
        int calculatedQuantity = getTotalQuantity();
        BigDecimal calculatedTotalPrice = getTotalSellingPrice();
        BigDecimal calculatedTotalMrpPrice = getCalculatedTotalMrpPrice();
        
        return quantity == calculatedQuantity
                && totalPrice.compareTo(calculatedTotalPrice) == 0
                && totalMrpPrice.compareTo(calculatedTotalMrpPrice) == 0;
    }

    public boolean isIncreasingItemQuantity(Long productId, int newQuantity) {
        return cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .map(item -> newQuantity > item.getQuantity())
                .orElse(newQuantity > 0);
    }

    public boolean isDecreasingItemQuantity(Long productId, int newQuantity) {
        return cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .anyMatch(item -> newQuantity < item.getQuantity());
    }

    public Cart markAsOrdered() {
        return withStatus(CartStatus.CHECKED_OUT);
    }

    public Cart markAsAbandoned() {
        return withStatus(CartStatus.ABANDONED);
    }

    public Cart withStatus(CartStatus newStatus) {
        return new Cart(
                id, user, sessionId, totalPrice, totalMrpPrice,
                quantity, discount, couponCode, newStatus, cartItems
        );
    }

    private Cart withCartItems(Set<CartItem> newCartItems) {
        BigDecimal newTotalPrice = calculateTotalSellingPrice(newCartItems);
        BigDecimal newTotalMrpPrice = calculateTotalMrpPrice(newCartItems);
        int newQuantity = calculateQuantity(newCartItems);
        
        return new Cart(
                id, user, sessionId, newTotalPrice, newTotalMrpPrice,
                newQuantity, discount, couponCode, status, newCartItems
        );
    }

    private static BigDecimal calculateTotalSellingPrice(Set<CartItem> items) {
        return items.stream()
                .map(item -> item.getSellingPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateTotalMrpPrice(Set<CartItem> items) {
        return items.stream()
                .map(item -> item.getMrpPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static int calculateQuantity(Set<CartItem> items) {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
                ", sessionId='" + sessionId + '\'' +
                ", totalPrice=" + totalPrice +
                ", totalMrpPrice=" + totalMrpPrice +
                ", quantity=" + quantity +
                ", discount=" + discount +
                ", couponCode='" + couponCode + '\'' +
                ", status=" + status +
                ", cartItems=" + cartItems +
                '}';
    }
}
