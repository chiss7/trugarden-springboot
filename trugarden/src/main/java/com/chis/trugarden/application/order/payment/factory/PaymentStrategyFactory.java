package com.chis.trugarden.application.order.payment.factory;

import com.chis.trugarden.application.order.payment.strategy.PaymentStrategy;
import com.chis.trugarden.shared.enums.PaymentProvider;
import com.chis.trugarden.shared.exception.UnsupportedPaymentProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {

    // Spring injects all beans that implement PaymentStrategy into this list
    private final List<PaymentStrategy> strategies;

    public PaymentStrategy getStrategy(PaymentProvider provider) {
        return strategies.stream()
                .filter(s -> s.provider() == provider)
                .findFirst()
                .orElseThrow(() -> new UnsupportedPaymentProviderException("No strategy found for provider: " + provider));
    }
}
