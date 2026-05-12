package com.chis.trugarden.api.order.create;

public record CreateOrderResponse(
        Long orderId,
        String paymentLink,
        int etaMinDays,
        int etaMaxDays
) {
}
