package com.chis.trugarden.application.order.create;

import com.chis.trugarden.application.cart.CartService;
import com.chis.trugarden.application.order.OrderService;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.cart.CartErrors;
import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.domain.user.Address;
import com.chis.trugarden.infrastructure.security.AuthenticationHelper;
import com.chis.trugarden.shared.enums.CartStatus;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderCommandHandler {
    private final CartService cartService;
    private final OrderService orderService;

    @CommandHandler
    public Result<CreateOrderResult> handle(CreateOrderCommand command) {
        boolean isAuthenticated = AuthenticationHelper.isAuthenticated();
        log.info("Creating order for {} with sessionId: {}", isAuthenticated ? "user " + AuthenticationHelper.getCurrentUserId() : "guest", command.sessionId());
        Result<Cart> cartResult = cartService.getUserCart(command.sessionId(), CartStatus.ACTIVE);
        if (cartResult.isFailure()) {
            log.error("Failed to retrieve cart for sessionId: {}. Error: {}", command.sessionId(), cartResult.getError());
            return Result.failure(cartResult.getError());
        }
        Cart cart = cartResult.getValue();

        if (cart.isNotActive()) {
            log.error("Retrieved cart is not active for sessionId: {}. Cart status: {}", command.sessionId(), cart.getStatus());
            return Result.failure(CartErrors.cartNotActive());
        }

        if (cart.isEmpty()) {
            log.error("User's cart is empty for sessionId: {}", command.sessionId());
            return Result.failure(CartErrors.emptyCart());
        }

        if (!cart.isValid()) {
            log.error("User has invalid cart");
            return Result.failure(CartErrors.invalidCart());
        }

        if (!isAuthenticated && !command.sessionId().equals(cart.getSessionId())) {
            log.error("Session ID mismatch. Command sessionId: {}, Cart sessionId: {}", command.sessionId(), cart.getSessionId());
            return Result.failure(CartErrors.sessionMismatch());
        }

        Set<CartItem> itemsWithoutStock = cart.getItemsWithoutStock();
        if (!itemsWithoutStock.isEmpty()) {
            log.error("User has items without stock in cart: {}", itemsWithoutStock);
            return Result.failure(CartErrors.itemsOutOfStock(itemsWithoutStock));
        }

        Address shippingAddress = Address.of(
                command.shippingAddressId(),
                command.principalStreet(),
                command.secondaryStreet(),
                command.houseNumber(),
                command.zipCode(),
                command.sector(),
                command.city(),
                isAuthenticated ? AuthenticationHelper.getCurrentUserId() : null,
                isAuthenticated ? null : command.sessionId()
        );

        Result<CreateOrderResult> orderResult = orderService.createOrder(cart, shippingAddress);
        if (orderResult.isFailure()) {
            log.error("Failed to create order for sessionId: {}. Error: {}", command.sessionId(), orderResult.getError());
            return Result.failure(orderResult.getError());
        }
        return Result.success(orderResult.getValue());
    }
}
