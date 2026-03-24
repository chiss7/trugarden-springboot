package com.chis.trugarden.application.cart;

import com.chis.trugarden.application.cart.abstractions.CartRepository;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.cart.CartErrors;
import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.infrastructure.security.AuthenticationHelper;
import com.chis.trugarden.shared.enums.CartStatus;
import com.chis.trugarden.shared.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Result<Cart> getUserCart(String sessionId) {
        boolean isAuthenticated = AuthenticationHelper.isAuthenticated();

        if (!isAuthenticated && sessionId != null) {
            Optional<Cart> cartOpt = cartRepository.findBySessionId(sessionId);
            return cartOpt.map(Result::success).orElseGet(() -> Result.success(getNotCreatedCart()));
        } else if (isAuthenticated) {
            Long userId = AuthenticationHelper.getCurrentUserId();
            Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
            return cartOpt.map(Result::success).orElseGet(() -> Result.success(getNotCreatedCart()));
        } else {
            return Result.failure(CartErrors.invalidAction());
        }
    }

    @Transactional
    public Result<Cart> createCart(String sessionId, Product product, int quantity) {
        boolean isAuthenticated = AuthenticationHelper.isAuthenticated();

        CartItem newCartItem = CartItem.newFromProduct(
                null,
                product,
                quantity,
                isAuthenticated ? AuthenticationHelper.getCurrentUserId() : null
        );

        Cart newCart = Cart.empty(
                isAuthenticated ? AuthenticationHelper.getCurrentUser().getDomainUser() : null,
                isAuthenticated ? null : sessionId,
                CartStatus.ACTIVE
        );

        Cart toSave = newCart.addItem(newCartItem);
        return Result.success(cartRepository.save(toSave));
    }

    @Transactional
    public Result<Cart> updateCartItem(Cart cart, Product product, int quantity, boolean isIncreasingItemQuantity) {
        if (!cart.isActive()) {
            return Result.failure(CartErrors.unableToUpdateCart());
        }

        if (!isIncreasingItemQuantity && !cart.hasItem(product.getId())) {
            return Result.failure(CartErrors.itemNotInCart(product.getId()));
        }

        Cart updatedCart;
        if (cart.hasItem(product.getId())) {
            updatedCart = cart.updateItemQuantity(product.getId(), quantity);
        } else {
            CartItem newCartItem = CartItem.newFromProduct(
                    cart.getId(),
                    product,
                    quantity,
                    cart.getUser() != null ? cart.getUser().getId() : null
            );
            updatedCart = cart.addItem(newCartItem);
        }

        return Result.success(cartRepository.save(updatedCart));
    }

    private Cart getNotCreatedCart() {
        return Cart.empty(null, null, CartStatus.NOT_CREATED);
    }
}
