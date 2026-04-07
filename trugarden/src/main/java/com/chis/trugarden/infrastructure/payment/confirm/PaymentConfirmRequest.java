package com.chis.trugarden.infrastructure.payment.confirm;

public record PaymentConfirmRequest(
        Long id,
        String clientTxId
) {}
