package com.chis.trugarden.application.order.payment.dtos;

import com.chis.trugarden.shared.enums.PaymentProvider;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResult {
    private String paymentUrl;
    private PaymentProvider provider;
    private String rawResponse;
}
