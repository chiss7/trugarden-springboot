package com.chis.trugarden.application.order.create;

public record CreateOrderResult(
        Long orderId,
        String paymentLink,
        int etaMinDays,
        int etaMaxDays
) {
}
