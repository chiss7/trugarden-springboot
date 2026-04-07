package com.chis.trugarden.application.order.create;

import com.chis.trugarden.application.cart.service.CartService;
import com.chis.trugarden.application.order.service.OrderService;
import com.chis.trugarden.application.stock.service.StockReservationService;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.cart.CartErrors;
import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.domain.user.Address;
import com.chis.trugarden.domain.user.UserErrors;
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
    private final StockReservationService stockReservationService;

    @CommandHandler
    public Result<CreateOrderResult> handle(CreateOrderCommand command) {
        boolean isAuthenticated = AuthenticationHelper.isAuthenticated();
        if (!isAuthenticated) {
            log.error("User is not authenticated");
            return Result.failure(UserErrors.notAuthenticated());
        }

        Long userId = AuthenticationHelper.getCurrentUserId();
        log.info("Creating order for user {}", userId);
        Result<Cart> cartResult = cartService.getUserCart(null, CartStatus.ACTIVE);
        if (cartResult.isFailure()) {
            log.error("Failed to retrieve cart for user: {}. Error: {}", userId, cartResult.getError());
            return Result.failure(cartResult.getError());
        }
        Cart cart = cartResult.getValue();

        if (cart.isNotActive()) {
            log.error("Retrieved cart is not active for user: {}. Cart status: {}", userId, cart.getStatus());
            return Result.failure(CartErrors.cartNotActive());
        }

        if (cart.isEmpty()) {
            log.error("User's cart is empty for userId: {}", userId);
            return Result.failure(CartErrors.emptyCart());
        }

        if (!cart.isValid()) {
            log.error("User has invalid cart");
            return Result.failure(CartErrors.invalidCart());
        }

        // Validate available stock considering active reservations
        Set<CartItem> itemsWithoutStock = stockReservationService.getItemsWithInsufficientStock(cart.getCartItems());
        if (!itemsWithoutStock.isEmpty()) {
            log.error("User has items without available stock in cart: {}", itemsWithoutStock);
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
                userId,
                null
        );

        Result<CreateOrderResult> orderResult = orderService.createOrder(cart, shippingAddress);
        if (orderResult.isFailure()) {
            log.error("Failed to create order for user: {}. Error: {}", AuthenticationHelper.getCurrentUserId(), orderResult.getError());
            return Result.failure(orderResult.getError());
        }
        return Result.success(orderResult.getValue());
    }
}
