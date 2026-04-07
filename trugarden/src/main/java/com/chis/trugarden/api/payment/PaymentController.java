package com.chis.trugarden.api.payment;

import com.chis.trugarden.api.payment.confirm.ConfirmPaymentMapper;
import com.chis.trugarden.api.payment.confirm.ConfirmPaymentRequest;
import com.chis.trugarden.application.order.payment.confirm.ConfirmPaymentResult;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import com.chis.trugarden.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment Controller")
public class PaymentController extends ControllerBase {
    private final CommandGateway commandGateway;
    private final ConfirmPaymentMapper confirmPaymentMapper;

    @PostMapping("/confirm")
    public ResponseEntity<GenericResponse<?>> confirmPayment(
            @RequestBody @Validated ConfirmPaymentRequest request
    ) {
        Result<ConfirmPaymentResult> result = commandGateway.sendAndWait(confirmPaymentMapper.toCommand(request));
        return result.isSuccess() ?
                success(confirmPaymentMapper.toResponse(result.getValue())) :
                error(result.getError());
    }
}
