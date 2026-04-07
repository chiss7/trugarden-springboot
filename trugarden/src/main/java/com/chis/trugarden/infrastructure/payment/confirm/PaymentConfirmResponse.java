package com.chis.trugarden.infrastructure.payment.confirm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmResponse {
    // For canceled payment
    private Long id;
    private String message;
    private Integer errorCode;

    // For successful payment
    private String email;
    private String cardType;
    private String bin;
    private String lastDigits;
    private String deferredCode;
    private String deferredMessage;
    private Boolean deferred;
    private String cardBrandCode;
    private String cardBrand;
    private Integer amount;
    private String clientTransactionId;
    private String phoneNumber;
    private Integer statusCode; // 3 = Approved, 2 = Canceled
    private String transactionStatus;
    private String authorizationCode;
    private Integer messageCode;
    private Long transactionId;
    private String document;
    private List<String> taxes;
    private String currency;
    private String optionalParameter1;
    private String optionalParameter2;
    private String optionalParameter3;
    private String optionalParameter4;
    private String storeName;
    private String date;
    private String regionIso;
    private String transactionType;
    private String recap;
    private String reference;
    private String pan;
}
