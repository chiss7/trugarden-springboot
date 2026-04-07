package com.chis.trugarden.api.payment.confirm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConfirmPaymentRequest(
        @NotNull(message = "Transaction ID cannot be null")
        Long id,

        @NotNull(message = "clientTransactionId cannot be null")
        @NotBlank(message = "clientTransactionId cannot be blank")
        String clientTransactionId
) {
}
