package com.chis.trugarden.application.cart.get;

import com.chis.trugarden.application.cart.CartService;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.infrastructure.security.AuthenticationHelper;
import com.chis.trugarden.shared.enums.CartStatus;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserCartQueryHandler {
    private final CartService cartService;

    @QueryHandler
    public GetUserCartQueryResult handle(GetUserCartQuery query) {
        boolean isAuthenticated = AuthenticationHelper.isAuthenticated();
        Result<Cart> userCartResult = cartService.getUserCart(query.sessionId(), CartStatus.ACTIVE);
        if (userCartResult.isFailure()) {
            return GetUserCartQueryResult.failure(userCartResult.getError());
        }
        return GetUserCartQueryResult.success(GetUserCartResult.from(userCartResult.getValue(), isAuthenticated));
    }
}
