package com.chis.trugarden.application.order.payment.service;

import com.chis.trugarden.application.cart.service.CartService;
import com.chis.trugarden.application.order.abstractions.OrderRepository;
import com.chis.trugarden.application.stock.service.StockReservationService;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.order.Order;
import com.chis.trugarden.domain.order.OrderErrors;
import com.chis.trugarden.domain.order.Payment;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmResponse;
import com.chis.trugarden.shared.enums.OrderStatus;
import com.chis.trugarden.shared.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;
    private final StockReservationService stockReservationService;
    private final CartService cartService;

    @Transactional
    public Result<Payment> handleCanceledPayment(PaymentConfirmResponse response) {
        Optional<Order> orderOpt = orderRepository.findByOrderId(response.getClientTransactionId());
        if (orderOpt.isEmpty()) {
            return Result.failure(OrderErrors.notFound(response.getClientTransactionId()));
        }

        Order order = orderOpt.get();

        Payment canceledPayment = order.getPayment()
                .withCanceledStatus(response.getMessage());
        Order canceledOrder = order
                .withStatus(OrderStatus.CANCELLED)
                .withUpdatedPayment(canceledPayment);

        Order savedOrder = orderRepository.save(canceledOrder);

        Result<Void> cancelReservationResult = stockReservationService.cancelReservations(savedOrder.getId());
        if (cancelReservationResult.isFailure()) {
            log.error("Failed to cancel stock reservations for order: {}", savedOrder.getId());
            return Result.failure(cancelReservationResult.getError());
        }

        Result<Cart> cartResult = cartService.restoreToActiveCart(null);
        if (cartResult.isFailure()) {
            log.error("Failed to restore cart for canceled order: {}", savedOrder.getId());
            return Result.failure(cartResult.getError());
        }

        log.info("Payment canceled successfully for order: {} (ID: {}). Stock reservations canceled.", 
                savedOrder.getOrderId(), savedOrder.getId());
        return Result.success(savedOrder.getPayment());
    }

    @Transactional
    public Result<Payment> handleSuccessfulPayment(PaymentConfirmResponse response) {
        Optional<Order> orderOpt = orderRepository.findByOrderId(response.getClientTransactionId());
        if (orderOpt.isEmpty()) {
            return Result.failure(OrderErrors.notFound(response.getClientTransactionId()));
        }

        Order order = orderOpt.get();

        // Confirmar reservas y DECREMENTAR stock real
        Result<Void> confirmResult = stockReservationService.confirmReservations(order.getId());
        if (confirmResult.isFailure()) {
            log.error("Failed to confirm stock reservations for order: {}", order.getId());
            return Result.failure(confirmResult.getError());
        }

        Payment completedPayment = order.getPayment()
                .withCompletedStatus(
                        String.valueOf(response.getTransactionId()),
                        response.getAuthorizationCode(),
                        response.getTransactionStatus()
                );

        Order confirmedOrder = order
                .withStatus(OrderStatus.CONFIRMED)
                .withUpdatedPayment(completedPayment);

        Order savedOrder = orderRepository.save(confirmedOrder);

        Result<Cart> cartResult = cartService.markCartAsCheckedOut(null);
        if (cartResult.isFailure()) {
            log.error("Failed to mark cart as checked out for order: {}", savedOrder.getId());
            return Result.failure(cartResult.getError());
        }

        log.info("Payment confirmed successfully for order: {} (ID: {}). Stock decremented.", 
                savedOrder.getOrderId(), savedOrder.getId());
        return Result.success(savedOrder.getPayment());
    }
}
