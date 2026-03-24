package com.chis.trugarden.api.cart;

import com.chis.trugarden.api.cart.update_cart_item.UpdateCartItemMapper;
import com.chis.trugarden.api.cart.update_cart_item.UpdateCartItemRequest;
import com.chis.trugarden.api.cart.get.GetUserCartMapper;
import com.chis.trugarden.application.cart.get.GetUserCartQuery;
import com.chis.trugarden.application.cart.get.GetUserCartQueryResult;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import com.chis.trugarden.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Controller")
public class CartController extends ControllerBase {
    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;
    private final GetUserCartMapper getUserCartMapper;
    private final UpdateCartItemMapper updateCartItemMapper;

    @GetMapping
    public ResponseEntity<GenericResponse<?>> findUserCart(
            @RequestAttribute(value = "SESSION_ID_ATTR", required = false) String sessionId
    ) {
        GetUserCartQuery query = new GetUserCartQuery(sessionId);
        GetUserCartQueryResult result = queryGateway.query(query, ResponseTypes.instanceOf(GetUserCartQueryResult.class)).join();
        return result.isSuccess() ?
                success(getUserCartMapper.toResponse(result.getValue())) :
                error(result.getError());
    }

    @PostMapping("/update-item")
    public ResponseEntity<GenericResponse<?>> updateCartItem(
            @RequestAttribute(value = "SESSION_ID_ATTR", required = false) String sessionId,
            @RequestBody @Validated UpdateCartItemRequest request
    ) {
        request.setSessionId(sessionId);
        Result<Long> result = commandGateway.sendAndWait(updateCartItemMapper.toCommand(request));
        return result.isSuccess() ?
                success(result.getValue()) :
                error(result.getError());
    }
}
