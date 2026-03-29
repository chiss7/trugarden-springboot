package com.chis.trugarden.api.order;

import com.chis.trugarden.api.order.create.CreateOrderMapper;
import com.chis.trugarden.api.order.create.CreateOrderRequest;
import com.chis.trugarden.application.order.create.CreateOrderResult;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import com.chis.trugarden.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "Order Controller")
public class OrderController extends ControllerBase {
    private final CommandGateway commandGateway;
    private final CreateOrderMapper createOrderMapper;

    @PostMapping
    public ResponseEntity<GenericResponse<?>> createOrder(
            @RequestAttribute(value = "SESSION_ID_ATTR", required = false) String sessionId,
            @RequestBody @Validated CreateOrderRequest request
    ) {
        request.setSessionId(sessionId);
        Result<CreateOrderResult> result = commandGateway.sendAndWait(createOrderMapper.toCommand(request));
        return result.isSuccess() ?
                success(createOrderMapper.toResponse(result.getValue())) :
                error(result.getError());
    }
}
