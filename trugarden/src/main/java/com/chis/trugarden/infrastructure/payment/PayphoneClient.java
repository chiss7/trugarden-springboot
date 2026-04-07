package com.chis.trugarden.infrastructure.payment;

import com.chis.trugarden.application.order.payment.dtos.payphone.PayphoneErrorResponse;
import com.chis.trugarden.application.order.payment.dtos.payphone.PayphoneRequest;
import com.chis.trugarden.application.order.payment.dtos.payphone.PayphoneResponse;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmRequest;
import com.chis.trugarden.infrastructure.payment.confirm.PaymentConfirmResponse;
import com.chis.trugarden.shared.exception.PaymentClientException;
import com.chis.trugarden.shared.exception.PaymentProviderException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.stream.Collectors;

@Slf4j
@Component
public class PayphoneClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public PayphoneClient(
            @Qualifier("payphoneRestClient") RestClient restClient,
            ObjectMapper objectMapper
    ) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public PayphoneResponse createPaymentLink(PayphoneRequest request) {
        try {
            return restClient.post()
                    .uri("/button/Prepare")
                    .body(request)
                    .retrieve()
                    .body(PayphoneResponse.class);

        } catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            log.error("Payphone client error: status={}, body={}", e.getStatusCode(), body);
            throw new PaymentClientException(parseErrorMessage(body));

        } catch (HttpServerErrorException e) {
            String body = e.getResponseBodyAsString();
            log.error("Payphone server error: status={}, body={}", e.getStatusCode(), body);
            throw new PaymentProviderException("Payphone server error: " + e.getStatusCode());

        } catch (RestClientException e) {
            log.error("Error connecting to Payphone: {}", e.getMessage(), e);
            throw new PaymentProviderException("Error connecting to Payphone", e);
        }
    }

    public PaymentConfirmResponse confirmPayment(Long id, String clientTransactionId) {
        try {
            return restClient.post()
                    .uri("/button/V2/Confirm")
                    .body(new PaymentConfirmRequest(id, clientTransactionId))
                    .retrieve()
                    .body(PaymentConfirmResponse.class);

        } catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            log.error("Payphone confirm error: status={}, body={}", e.getStatusCode(), body);
            throw new PaymentClientException(parseErrorMessage(body));

        } catch (HttpServerErrorException e) {
            log.error("Payphone confirm server error: {}", e.getStatusCode());
            throw new PaymentProviderException("Payphone server error al confirmar: " + e.getStatusCode());
        }
    }

    private String parseErrorMessage(String body) {
        try {
            PayphoneErrorResponse error = objectMapper.readValue(body, PayphoneErrorResponse.class);

            String details = error.getErrors().stream()
                    .flatMap(e -> e.getErrorDescriptions().stream())
                    .collect(Collectors.joining(", "));

            return "Payphone error [%d]: %s — %s".formatted(
                    error.getErrorCode(), error.getMessage(), details);

        } catch (Exception e) {
            return "Payphone error: " + body;
        }
    }
}
