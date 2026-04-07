package com.chis.trugarden.application.order.payment.confirm;

import com.chis.trugarden.shared.enums.PaymentStatus;

public record ConfirmPaymentResult(
        Long orderId,
        String message,
        PaymentStatus paymentStatus
) {
}
