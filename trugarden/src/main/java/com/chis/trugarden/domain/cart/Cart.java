package com.chis.trugarden.domain.cart;

import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.shared.enums.CartStatus;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@AggregateRoot
public class Cart {
    private final Long id;
    private final User user;
    private final String sessionId;
    private final BigDecimal subtotal;
    private final BigDecimal totalTax;
    private final BigDecimal discountPercentage;
    private final BigDecimal couponDiscountAmount;
    private final BigDecimal grandTotal;
    private final int quantity;
    private final String couponCode;
    private final CartStatus status;
    private final Set<CartItem> cartItems;

    public Cart(
            Long id,
            User user,
            String sessionId,
            BigDecimal subtotal,
            BigDecimal totalTax,
            BigDecimal discountPercentage,
            BigDecimal couponDiscountAmount,
            BigDecimal grandTotal,
            int quantity,
            String couponCode,
            CartStatus status,
            Set<CartItem> cartItems
    ) {
        this.id = id;
        this.user = user;
        this.sessionId = sessionId;
        this.subtotal = Objects.requireNonNull(subtotal, "subtotal cannot be null");
        this.totalTax = Objects.requireNonNull(totalTax, "totalTax cannot be null");
        this.discountPercentage = Objects.requireNonNull(discountPercentage, "discountPercentage cannot be null");
        this.couponDiscountAmount = Objects.requireNonNull(couponDiscountAmount, "couponDiscountAmount cannot be null");
        this.grandTotal = Objects.requireNonNull(grandTotal, "grandTotal cannot be null");
        this.quantity = quantity;
        this.couponCode = couponCode;
        this.status = status;
        this.cartItems = cartItems;
    }

    public static Cart of(
            Long id,
            User user,
            String sessionId,
            BigDecimal subtotal,
            BigDecimal totalTax,
            BigDecimal discountPercentage,
            BigDecimal couponDiscountAmount,
            BigDecimal grandTotal,
            int quantity,
            String couponCode,
            CartStatus status,
            Set<CartItem> cartItems
    ) {
        return new Cart(
                id,
                user,
                sessionId,
                subtotal,
                totalTax,
                discountPercentage,
                couponDiscountAmount,
                grandTotal,
                quantity,
                couponCode,
                status,
                cartItems
        );
    }

    public static Cart ofNew(
            User user,
            String sessionId,
            BigDecimal subtotal,
            BigDecimal totalTax,
            BigDecimal discountPercentage,
            BigDecimal couponDiscountAmount,
            BigDecimal grandTotal,
            int quantity,
            String couponCode,
            CartStatus status,
            Set<CartItem> cartItems
    ) {
        return new Cart(
                null,
                user,
                sessionId,
                subtotal,
                totalTax,
                discountPercentage,
                couponDiscountAmount,
                grandTotal,
                quantity,
                couponCode,
                status,
                cartItems
        );
    }

    /**
     * Creates an empty cart with zero values.
     */
    public static Cart empty(User user, String sessionId, CartStatus status) {
        return new Cart(
                null,
                user,
                sessionId,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0,
                null,
                status,
                new HashSet<>()
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public BigDecimal getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public int getQuantity() {
        return quantity;
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
        return recalculate(newItems);
    }

    public Cart removeItem(Long productId) {
        Set<CartItem> newItems = cartItems.stream()
                .filter(item -> !item.getProduct().getId().equals(productId))
                .collect(Collectors.toSet());
        return recalculate(newItems);
    }

    public Cart updateItemQuantity(Long productId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeItem(productId);
        }
        Set<CartItem> newItems = cartItems.stream()
                .map(item -> item.getProduct().getId().equals(productId)
                        ? item.withQuantity(newQuantity)
                        : item)
                .collect(Collectors.toSet());
        return recalculate(newItems);
    }

    public Cart clear() {
        return recalculate(new HashSet<>());
    }

    /**
     * Calculates the total subtotal (base amount without tax) from all cart items.
     */
    public BigDecimal getCalculatedSubtotal() {
        return cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the total amount with tax from all cart items.
     * Since subtotal is the base amount (without tax), this returns subtotal + totalTax.
     */
    public BigDecimal getTotalWithTax() {
        return cartItems.stream()
                .map(CartItem::getTotalWithTax)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the total tax to add to the subtotal from all cart items.
     */
    public BigDecimal getCalculatedTotalTax() {
        return cartItems.stream()
                .map(CartItem::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalQuantity() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Applies a coupon with the given percentage discount.
     * The coupon is applied to the subtotal (base amount before tax).
     */
    public Cart applyCoupon(String code, BigDecimal percentage) {
        BigDecimal newDiscountPercentage = Objects.requireNonNull(percentage, "percentage cannot be null");
        BigDecimal newCouponDiscountAmount = subtotal.multiply(newDiscountPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        // Recalculate tax on the discounted base
        BigDecimal discountedBase = subtotal.subtract(newCouponDiscountAmount);
        BigDecimal adjustedTax = calculateTaxOnBase(discountedBase);
        BigDecimal newGrandTotal = discountedBase.add(adjustedTax);

        return new Cart(
                id, user, sessionId, subtotal, adjustedTax,
                newDiscountPercentage, newCouponDiscountAmount, newGrandTotal,
                quantity, Objects.requireNonNull(code),
                status, cartItems
        );
    }

    public Cart removeCoupon() {
        return recalculate(cartItems);
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public boolean isActive() {
        return status == CartStatus.ACTIVE;
    }

    public boolean isNotActive() {
        return status != CartStatus.ACTIVE;
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
            return subtotal.compareTo(BigDecimal.ZERO) == 0
                    && totalTax.compareTo(BigDecimal.ZERO) == 0
                    && grandTotal.compareTo(BigDecimal.ZERO) == 0
                    && quantity == 0;
        }

        int calculatedQuantity = getTotalQuantity();
        BigDecimal calculatedSubtotal = getCalculatedSubtotal();
        BigDecimal calculatedTax = getCalculatedTotalTax();

        boolean basicValidation =
                quantity == calculatedQuantity &&
                        subtotal.compareTo(calculatedSubtotal) == 0 &&
                        totalTax.compareTo(calculatedTax) == 0;

        BigDecimal expectedGrandTotal;

        if (couponCode != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal expectedDiscount = subtotal
                    .multiply(discountPercentage)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            if (couponDiscountAmount.compareTo(expectedDiscount) != 0) {
                return false;
            }

            BigDecimal discountedBase = subtotal.subtract(couponDiscountAmount);
            BigDecimal expectedTax = calculateTaxOnBase(discountedBase);
            expectedGrandTotal = discountedBase.add(expectedTax);

            return basicValidation
                    && totalTax.compareTo(expectedTax) == 0
                    && grandTotal.compareTo(expectedGrandTotal) == 0;
        }

        // No coupon
        expectedGrandTotal = subtotal.add(totalTax);

        return basicValidation
                && grandTotal.compareTo(expectedGrandTotal) == 0;
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

    public Cart markAsPendingPayment() {
        return withStatus(CartStatus.PENDING_PAYMENT);
    }

    public Cart markAsAbandoned() {
        return withStatus(CartStatus.ABANDONED);
    }

    public Cart withStatus(CartStatus newStatus) {
        return new Cart(
                id, user, sessionId, subtotal, totalTax,
                discountPercentage, couponDiscountAmount, grandTotal,
                quantity, couponCode, newStatus, cartItems
        );
    }

    public Cart withUser(User user) {
        return new Cart(
                id, user, sessionId, subtotal, totalTax,
                discountPercentage, couponDiscountAmount, grandTotal,
                quantity, couponCode, status, cartItems
        );
    }

    /**
     * Recalculates all cart totals based on the given items.
     * subtotal = sum of item subtotals (base prices without tax)
     * totalTax = sum of item taxes
     * grandTotal = subtotal + totalTax
     * Resets coupon discount when items change.
     */
    private Cart recalculate(Set<CartItem> newCartItems) {
        BigDecimal newSubtotal = calculateSubtotal(newCartItems);
        BigDecimal newTotalTax = calculateTotalTax(newCartItems);
        int newQuantity = calculateQuantity(newCartItems);
        BigDecimal newGrandTotal = newSubtotal.add(newTotalTax);

        return new Cart(
                id, user, sessionId, newSubtotal, newTotalTax,
                BigDecimal.ZERO, BigDecimal.ZERO, newGrandTotal,
                newQuantity, null, // Reset coupon when items change
                status, newCartItems
        );
    }

    /**
     * Calculates the average tax rate across all items (weighted by subtotal).
     * Since subtotal is the base amount (without tax), this calculates: totalTax / subtotal
     */
    private BigDecimal getAverageTaxRate() {
        BigDecimal calculatedSubtotal = getCalculatedSubtotal();
        if (calculatedSubtotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalTaxAmount = getCalculatedTotalTax();
        return totalTaxAmount.divide(calculatedSubtotal, 4, RoundingMode.HALF_UP);
    }

    /**
     * Calculates tax on a given base amount using the cart's average tax rate.
     */
    private BigDecimal calculateTaxOnBase(BigDecimal baseAmount) {
        return baseAmount.multiply(getAverageTaxRate()).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateSubtotal(Set<CartItem> items) {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateTotalTax(Set<CartItem> items) {
        return items.stream()
                .map(CartItem::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static int calculateQuantity(Set<CartItem> items) {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    /**
     * @deprecated Use StockReservationService.getItemsWithInsufficientStock() instead.
     * This method only checks physical stock, not available stock (considering active reservations).
     */
    @Deprecated
    public Set<CartItem> getItemsWithoutStock() {
        return cartItems.stream()
                .filter(item -> item.getQuantity() > item.getProduct().getStock())
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
                ", sessionId='" + sessionId + '\'' +
                ", subtotal=" + subtotal +
                ", totalTax=" + totalTax +
                ", discountPercentage=" + discountPercentage +
                ", couponDiscountAmount=" + couponDiscountAmount +
                ", grandTotal=" + grandTotal +
                ", quantity=" + quantity +
                ", couponCode='" + couponCode + '\'' +
                ", status=" + status +
                ", cartItems=" + cartItems +
                '}';
    }
}
