package com.chis.trugarden.domain.order;

import com.chis.trugarden.shared.enums.Currency;
import com.chis.trugarden.shared.enums.PaymentStatus;

import java.math.BigDecimal;

public class Payment {
    private final Long id;
    private final Long orderId;
    private final String paymentId; // payphone payment ID
    private final String paymentUrl; // generated payment URL for Payphone
    private final BigDecimal amount;
    private final Currency currency;
    private final PaymentStatus status;
    private final String authorizationCode;   //  if its approved, the authorization code provided by Payphone
    private final String transactionStatus;   //  status transaction from payphone
    private final String failureReason;       //  error message if the payment failed

    public Payment(
            Long id,
            Long orderId,
            String paymentId,
            String paymentUrl,
            BigDecimal amount,
            Currency currency,
            PaymentStatus status,
            String authorizationCode,
            String transactionStatus,
            String failureReason
    ) {
        this.id = id;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.paymentUrl = paymentUrl;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.authorizationCode = authorizationCode;
        this.transactionStatus = transactionStatus;
        this.failureReason = failureReason;
    }

    public static Payment of(
            Long id,
            Long orderId,
            String paymentId,
            String paymentUrl,
            BigDecimal amount,
            Currency currency,
            PaymentStatus status,
            String authorizationCode,
            String transactionStatus,
            String failureReason
    ) {
        return new Payment(
                id, orderId, paymentId, paymentUrl, amount, currency, status, authorizationCode, transactionStatus, failureReason
        );
    }

    public static Payment ofNew(
            Long orderId,
            BigDecimal amount,
            Currency currency,
            PaymentStatus status
    ) {
        return new Payment(
                null,
                orderId,
                null,
                null,
                amount,
                currency,
                status,
                null,
                null,
                null
        );
    }

    public Payment withPaymentLink(
            String paymentUrl
    ) {
        return new Payment(
                this.id,
                this.orderId,
                this.paymentId,
                paymentUrl,
                this.amount,
                this.currency,
                this.status,
                this.authorizationCode,
                this.transactionStatus,
                this.failureReason
        );
    }

    public Payment withFailedStatus(
            String failureReason
    ) {
        return new Payment(
                this.id,
                this.orderId,
                this.paymentId,
                this.paymentUrl,
                this.amount,
                this.currency,
                PaymentStatus.FAILED,
                this.authorizationCode,
                this.transactionStatus,
                failureReason
        );
    }

    public Payment withCompletedStatus(
            String paymentId,
            String authorizationCode,
            String transactionStatus
    ) {
        return new Payment(
                this.id,
                this.orderId,
                paymentId,
                this.paymentUrl,
                this.amount,
                this.currency,
                PaymentStatus.COMPLETED,
                authorizationCode,
                transactionStatus,
                null
        );
    }

    public Payment withCanceledStatus(String failureReason) {
        return new Payment(
                this.id,
                this.orderId,
                null,
                this.paymentUrl,
                this.amount,
                this.currency,
                PaymentStatus.CANCELED,
                null,
                null,
                failureReason
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", paymentId='" + paymentId + '\'' +
                ", paymentUrl='" + paymentUrl + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                ", authorizationCode='" + authorizationCode + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
