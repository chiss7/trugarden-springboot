package com.chis.trugarden.api.payment.confirm;

import com.chis.trugarden.application.order.payment.confirm.ConfirmPaymentCommand;
import com.chis.trugarden.application.order.payment.confirm.ConfirmPaymentResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfirmPaymentMapper {
    ConfirmPaymentCommand toCommand(ConfirmPaymentRequest request);
    ConfirmPaymentResponse toResponse(ConfirmPaymentResult result);
}
