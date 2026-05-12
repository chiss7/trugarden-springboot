package com.chis.trugarden.application.cart.update_cart_item;

import com.chis.trugarden.application.cart.service.CartService;
import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.application.stock.service.StockReservationService;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.cart.CartErrors;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.domain.product.ProductErrors;
import com.chis.trugarden.infrastructure.security.AuthenticationHelper;
import com.chis.trugarden.shared.enums.ProductType;
import com.chis.trugarden.shared.properties.StockProperties;
import com.chis.trugarden.shared.enums.CartStatus;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCartItemCommandHandler {
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final StockProperties stockProperties;
    private final StockReservationService stockReservationService;

    @CommandHandler
    public Result<UpdateCartItemResult> handle(UpdateCartItemCommand command) {
        boolean isAuthenticated = AuthenticationHelper.isAuthenticated();
        Result<Cart> userCartResult = cartService.getUserCart(command.sessionId(), CartStatus.ACTIVE);
        if (userCartResult.isFailure()) {
            return Result.failure(userCartResult.getError());
        }

        Optional<Product> productOpt = productRepository.findById(command.productId());
        if (productOpt.isEmpty()) {
            return Result.failure(ProductErrors.notFound(command.productId()));
        }
        Product product = productOpt.get();
        Cart userCart = userCartResult.getValue();

        boolean isIncreasingItemQuantity = userCart.isIncreasingItemQuantity(product.getId(), command.quantity());

        if (isIncreasingItemQuantity) {
            if (product.getProductType() == ProductType.MADE_TO_ORDER) {
                int maxAllowed = stockProperties.getMaxMadeToOrderQuantityPerOrder();

                if (command.quantity() > maxAllowed) {
                    return Result.failure(CartErrors.madeToOrderLimitExceeded(
                            maxAllowed
                    ));
                }
            } else {
                int availableStock = stockReservationService.getAvailableStock(product.getId());
                if (command.quantity() > availableStock) {
                    return Result.failure(CartErrors.outOfStock());
                }
            }
        }

        if (userCart.isNotCreated()) {
            Result<Cart> newCart = cartService.createCart(command.sessionId(), product, command.quantity());
            return newCart.isSuccess() ?
                    Result.success(UpdateCartItemResult.from(newCart.getValue(), isAuthenticated)) :
                    Result.failure(newCart.getError());
        }

        Result<Cart> updatedCart = cartService.updateCartItem(userCart, product, command.quantity(), isIncreasingItemQuantity);
        return updatedCart.isSuccess() ?
                Result.success(UpdateCartItemResult.from(updatedCart.getValue(), isAuthenticated)) :
                Result.failure(updatedCart.getError());
    }
}
