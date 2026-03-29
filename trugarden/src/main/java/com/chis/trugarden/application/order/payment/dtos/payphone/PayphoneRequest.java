package com.chis.trugarden.application.order.payment.dtos.payphone;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class PayphoneRequest {
    private int amount;
    private int amountWithTax;
    private int amountWithoutTax;
    private int tax;
    private String currency;
    private String clientTransactionId;
    private String responseUrl;
    private String cancellationUrl;
    private String storeId;
    private String reference;
}
