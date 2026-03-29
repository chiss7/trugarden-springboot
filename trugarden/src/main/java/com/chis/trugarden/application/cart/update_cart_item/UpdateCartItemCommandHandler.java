package com.chis.trugarden.application.cart.update_cart_item;

import com.chis.trugarden.application.cart.CartService;
import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.cart.CartErrors;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.domain.product.ProductErrors;
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

    @CommandHandler
    public Result<Long> handle(UpdateCartItemCommand command) {
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

        if (userCart.isNotCreated()) {
            Result<Cart> newCart = cartService.createCart(command.sessionId(), product, command.quantity());
            return newCart.isSuccess() ?
                    Result.success(newCart.getValue().getId()) :
                    Result.failure(newCart.getError());
        }

        boolean isIncreasingItemQuantity = userCart.isIncreasingItemQuantity(product.getId(), command.quantity());

        if (isIncreasingItemQuantity && command.quantity() > product.getStock()) {
            return Result.failure(CartErrors.outOfStock());
        }

        Result<Cart> updatedCart = cartService.updateCartItem(userCart, product, command.quantity(), isIncreasingItemQuantity);
        return updatedCart.isSuccess() ?
                Result.success(updatedCart.getValue().getId()) :
                Result.failure(updatedCart.getError());
    }
}
