package com.chis.trugarden.application.order.payment.dtos.payphone;

import lombok.Data;

import java.util.List;

@Data
public class PayphoneErrorResponse {
    private String message;
    private int errorCode;
    private List<PayphoneErrorDetail> errors;
}
