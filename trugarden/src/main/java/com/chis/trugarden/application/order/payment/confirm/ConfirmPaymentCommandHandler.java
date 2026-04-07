package com.chis.trugarden.application.order.payment.confirm;

import com.chis.trugarden.application.order.payment.service.PaymentService;
import com.chis.trugarden.application.order.payment.factory.PaymentStrategyFactory;
import com.chis.trugarden.application.order.payment.strategy.PaymentStrategy;
import com.chis.trugarden.domain.order.OrderErrors;
import com.chis.trugarden.domain.order.Payment;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmRequest;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmResponse;
import com.chis.trugarden.shared.enums.PaymentProvider;
import com.chis.trugarden.shared.enums.PaymentStatus;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmPaymentCommandHandler {
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentService paymentService;

    @CommandHandler
    public Result<ConfirmPaymentResult> handle(ConfirmPaymentCommand command) {
        log.info("Confirming payment for id {}, clientTransactionId {}", command.id(), command.clientTransactionId());

        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(PaymentProvider.PAYPHONE);
        PaymentConfirmResponse response = paymentStrategy.confirmPayment(new PaymentConfirmRequest(command.id(), command.clientTransactionId()));
        log.info("Received payment confirmation response: {}", response);

        if (isCanceledPayment(response)) {
            Result<Payment> paymentResult = paymentService.handleCanceledPayment(response);
            if (paymentResult.isFailure()) {
                return Result.failure(paymentResult.getError());
            }
            return Result.success(new ConfirmPaymentResult(response.getId(), "Pago cancelado satisfactoriamente", PaymentStatus.CANCELED));
        }

        if (isSuccessfulPayment(response)) {
            Result<Payment> paymentResult = paymentService.handleSuccessfulPayment(response);
            if (paymentResult.isFailure()) {
                return Result.failure(paymentResult.getError());
            }
            return Result.success(new ConfirmPaymentResult(response.getId(), "Pago aprobado satisfactoriamente", PaymentStatus.COMPLETED));
        }

        log.warn("Unexpected payment confirmation status: {} for clientTransactionId: {}", response.getStatusCode(), response.getClientTransactionId());
        return Result.failure(OrderErrors.invalidStatus());
    }

    private boolean isCanceledPayment(PaymentConfirmResponse response) {
        return response.getId() == 0 || response.getStatusCode() == 2;
    }

    private boolean isSuccessfulPayment(PaymentConfirmResponse response) {
        return response.getStatusCode() == 3;
    }
}
