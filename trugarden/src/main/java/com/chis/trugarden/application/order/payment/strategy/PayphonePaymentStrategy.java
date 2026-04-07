package com.chis.trugarden.application.order.payment.strategy;

import com.chis.trugarden.application.order.payment.dtos.PaymentRequest;
import com.chis.trugarden.application.order.payment.dtos.PaymentResult;
import com.chis.trugarden.application.order.payment.dtos.payphone.PayphoneRequest;
import com.chis.trugarden.application.order.payment.dtos.payphone.PayphoneResponse;
import com.chis.trugarden.infrastructure.payment.PayphoneClient;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmRequest;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmResponse;
import com.chis.trugarden.shared.enums.PaymentProvider;
import com.chis.trugarden.shared.properties.PayphoneProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayphonePaymentStrategy implements PaymentStrategy {

    private final PayphoneClient payphoneClient;
    private final PayphoneProperties props;
    private final ObjectMapper objectMapper;

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.PAYPHONE;
    }

    @Override
    public PaymentResult createPaymentLink(PaymentRequest request) {
        log.info("Creating Payphone payment link for {}", request);
        PayphoneRequest payphoneRequest = toPayphoneRequest(request);
        PayphoneResponse response = payphoneClient.createPaymentLink(payphoneRequest);

        log.info("Payphone response for orderId={}: {}", request.getOrderId(), toJson(response));
        return PaymentResult.builder()
                .paymentUrl(response.getPayUrl())
                .provider(PaymentProvider.PAYPHONE)
                .rawResponse(toJson(response))
                .build();
    }

    @Override
    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request) {
        return payphoneClient.confirmPayment(request.id(), request.clientTxId());
    }

    private PayphoneRequest toPayphoneRequest(PaymentRequest req) {
        return PayphoneRequest.builder()
                .amount(toCents(req.getAmount()))
                .amountWithoutTax(toCents(req.getAmountWithoutTax()))
                .amountWithTax(toCents(req.getAmountWithTax()))
                .tax(toCents(req.getTax()))
                .currency(req.getCurrency())
                .clientTransactionId(req.getOrderId())
                .responseUrl(req.getCallbackUrl())
                .cancellationUrl(req.getCancellationUrl())
                .storeId(props.getStoreId())
                .reference(req.getDescription())
                .build();
    }

    private int toCents(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).intValue();
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }
}
