package com.chis.trugarden.infrastructure.payment.confirm;

public record PayphoneConfirmRequest(
        Long id,
        String clientTxId
) {}
