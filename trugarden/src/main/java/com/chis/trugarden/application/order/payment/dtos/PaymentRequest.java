package com.chis.trugarden.application.order.payment.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
public class PaymentRequest {
    private String orderId;
    private BigDecimal amount;
    private BigDecimal amountWithoutTax;
    private BigDecimal amountWithTax;
    private BigDecimal tax;
    private String currency;
    private String description;
    private String callbackUrl;
    private String cancellationUrl;
    private String additionalData;
}
