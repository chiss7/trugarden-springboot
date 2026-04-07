package com.chis.trugarden.application.order.payment.strategy;

import com.chis.trugarden.application.order.payment.dtos.PaymentRequest;
import com.chis.trugarden.application.order.payment.dtos.PaymentResult;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmRequest;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmResponse;
import com.chis.trugarden.shared.enums.PaymentProvider;

public interface PaymentStrategy {
    PaymentResult createPaymentLink(PaymentRequest request);
    PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request);
    PaymentProvider provider();
}
