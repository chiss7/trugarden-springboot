package com.chis.trugarden.application.order.payment.dtos.payphone;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PayphoneResponse {
    private String paymentId;
    private String payWithPayPhone;
    private String payWithCard;
    private int errorCode;
    private String message;
    private List<PayphoneErrorDetail> errors;

    public String getPayUrl() {
        return payWithCard;
    }

    public static PayphoneResponse error(PayphoneErrorResponse error) {
        return PayphoneResponse.builder()
                .message(error.getMessage())
                .errorCode(error.getErrorCode())
                .errors(error.getErrors())
                .build();
    }

    public boolean isSuccessful() {
        return payWithCard != null && !payWithCard.isBlank();
    }
}
