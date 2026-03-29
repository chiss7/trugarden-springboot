package com.chis.trugarden.application.order.payment.dtos.payphone;

import lombok.Data;

import java.util.List;

@Data
public class PayphoneErrorDetail {
    private String message;
    private int errorCode;
    private List<String> errorDescriptions;
}
