package com.chis.trugarden.persistence.order;

import com.chis.trugarden.domain.order.Payment;
import com.chis.trugarden.persistence.order.entities.PaymentOrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentOrderEntityMapper {
    default PaymentOrderEntity toEntity(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentOrderEntity entity = new PaymentOrderEntity();
        entity.setId(payment.getId());
        entity.setPaymentId(payment.getPaymentId());
        entity.setPaymentUrl(payment.getPaymentUrl());
        entity.setAmount(payment.getAmount());
        entity.setCurrency(payment.getCurrency());
        entity.setStatus(payment.getStatus());
        entity.setAuthorizationCode(payment.getAuthorizationCode());
        entity.setTransactionStatus(payment.getTransactionStatus());
        entity.setFailureReason(payment.getFailureReason());
        return entity;
    }

    default Payment toDomain(PaymentOrderEntity entity) {
        if (entity == null) {
            return null;
        }

        return Payment.of(
                entity.getId(),
                entity.getOrder().getId(),
                entity.getPaymentId(),
                entity.getPaymentUrl(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getAuthorizationCode(),
                entity.getTransactionStatus(),
                entity.getFailureReason()
        );
    }
}
