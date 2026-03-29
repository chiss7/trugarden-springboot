package com.chis.trugarden.infrastructure.payment.confirm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayphoneConfirmResponse {
    private String transactionStatus;
    private Integer statusCode; // 3 = Approved, 2 = Canceled
    private Long transactionId;
    private String clientTransactionId;
    private String authorizationCode;
    private Integer amount;
    private String cardBrand;
    private String message;
}
