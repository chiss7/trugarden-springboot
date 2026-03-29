package com.chis.trugarden.application.order.payment.strategy;

import com.chis.trugarden.application.order.payment.dtos.PaymentRequest;
import com.chis.trugarden.application.order.payment.dtos.PaymentResult;
import com.chis.trugarden.shared.enums.PaymentProvider;

public interface PaymentStrategy {
    PaymentResult createPaymentLink(PaymentRequest request);
    PaymentProvider provider();
}
