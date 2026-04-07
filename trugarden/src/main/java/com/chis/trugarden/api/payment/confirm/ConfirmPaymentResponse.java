package com.chis.trugarden.api.payment.confirm;

public record ConfirmPaymentResponse(
        Long orderId,
        String message,
        String status
) {
}
