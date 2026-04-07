package com.chis.trugarden.domain.order;

import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.user.Address;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.shared.enums.OrderStatus;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Order {
    private final Long id;
    private final String orderId;
    private final User user;
    private final String sessionId;
    private final BigDecimal subtotal;
    private final BigDecimal totalTax;
    private final BigDecimal discountPercentage;
    private final BigDecimal couponDiscountAmount;
    private final BigDecimal grandTotal;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderDate;
    private final LocalDateTime deliveryDate;
    private final List<OrderItem> orderItems;
    private final Address shippingAddress;
    private final Payment payment;

    public Order(
            Long id,
            String orderId,
            User user,
            String sessionId,
            BigDecimal subtotal,
            BigDecimal totalTax,
            BigDecimal discountPercentage,
            BigDecimal couponDiscountAmount,
            BigDecimal grandTotal,
            OrderStatus orderStatus,
            LocalDateTime orderDate,
            LocalDateTime deliveryDate,
            List<OrderItem> orderItems,
            Address shippingAddress,
            Payment payment
    ) {
        this.id = id;
        this.orderId = orderId;
        this.user = user;
        this.sessionId = sessionId;
        this.subtotal = subtotal;
        this.totalTax = totalTax;
        this.discountPercentage = discountPercentage;
        this.couponDiscountAmount = couponDiscountAmount;
        this.grandTotal = grandTotal;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderItems = orderItems;
        this.shippingAddress = shippingAddress;
        this.payment = payment;
    }

    public static Order of(
            Long id,
            String orderId,
            User user,
            String sessionId,
            BigDecimal subtotal,
            BigDecimal totalTax,
            BigDecimal discountPercentage,
            BigDecimal couponDiscountAmount,
            BigDecimal grandTotal,
            OrderStatus orderStatus,
            LocalDateTime orderDate,
            LocalDateTime deliveryDate,
            List<OrderItem> orderItems,
            Address shippingAddress,
            Payment payment
    ) {
        return new Order(
                id,
                orderId,
                user,
                sessionId,
                subtotal,
                totalTax,
                discountPercentage,
                couponDiscountAmount,
                grandTotal,
                orderStatus,
                orderDate,
                deliveryDate,
                orderItems,
                shippingAddress,
                payment
        );
    }

    public static Order ofNew(
            String orderId,
            User user,
            String sessionId,
            BigDecimal subtotal,
            BigDecimal totalTax,
            BigDecimal discountPercentage,
            BigDecimal couponDiscountAmount,
            BigDecimal grandTotal,
            OrderStatus orderStatus,
            LocalDateTime orderDate,
            LocalDateTime deliveryDate,
            List<OrderItem> orderItems,
            Address shippingAddress,
            Payment payment
    ) {
        return new Order(
                null,
                orderId,
                user,
                sessionId,
                subtotal,
                totalTax,
                discountPercentage,
                couponDiscountAmount,
                grandTotal,
                orderStatus,
                orderDate,
                deliveryDate,
                orderItems,
                shippingAddress,
                payment
        );
    }

    public Long getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Payment getPayment() {
        return payment;
    }

    public String getSessionId() {
        return sessionId;
    }

    private static String generateOrderId() {
        return UUID.randomUUID().toString();
    }

    public static Order newFromCart(Cart cart, Address shippingAddress, Payment payment) {
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        if (cart.isNotActive()) {
            throw new IllegalStateException("El carrito no está activo");
        }

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> OrderItem.fromCartItem(cartItem, null))
                .toList();

        BigDecimal subtotal = cart.getSubtotal();
        BigDecimal totalTax = cart.getTotalTax();
        BigDecimal discountPercentage = cart.getDiscountPercentage();
        BigDecimal couponDiscountAmount = cart.getCouponDiscountAmount();
        BigDecimal grandTotal = cart.getGrandTotal();

        return Order.ofNew(
                generateOrderId(),
                cart.getUser(),
                cart.getSessionId(),
                subtotal,
                totalTax,
                discountPercentage,
                couponDiscountAmount,
                grandTotal,
                OrderStatus.PENDING,
                LocalDateTime.now(),
                null, // deliveryDate
                orderItems,
                shippingAddress,
                payment
        );
    }



    public Order withUpdatedPayment(Payment newPayment) {
        return Order.of(
                this.id,
                this.orderId,
                this.user,
                this.sessionId,
                this.subtotal,
                this.totalTax,
                this.discountPercentage,
                this.couponDiscountAmount,
                this.grandTotal,
                this.orderStatus,
                this.orderDate,
                this.deliveryDate,
                this.orderItems,
                this.shippingAddress,
                newPayment
        );
    }

    public Order withStatus(OrderStatus newStatus) {
        return Order.of(
                this.id,
                this.orderId,
                this.user,
                this.sessionId,
                this.subtotal,
                this.totalTax,
                this.discountPercentage,
                this.couponDiscountAmount,
                this.grandTotal,
                newStatus,
                this.orderDate,
                this.deliveryDate,
                this.orderItems,
                this.shippingAddress,
                this.payment
        );
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", user=" + user +
                ", sessionId=" + sessionId +
                ", subtotal=" + subtotal +
                ", totalTax=" + totalTax +
                ", discountPercentage=" + discountPercentage +
                ", couponDiscountAmount=" + couponDiscountAmount +
                ", grandTotal=" + grandTotal +
                ", orderStatus=" + orderStatus +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", orderItems=" + orderItems +
                ", shippingAddress=" + shippingAddress +
                ", payment=" + payment +
                '}';
    }
}
