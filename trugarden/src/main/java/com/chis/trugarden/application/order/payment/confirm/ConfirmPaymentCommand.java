package com.chis.trugarden.application.order.payment.confirm;

public record ConfirmPaymentCommand(
        Long id,
        String clientTransactionId
) {
}
