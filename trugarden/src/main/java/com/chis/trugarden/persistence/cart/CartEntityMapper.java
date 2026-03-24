package com.chis.trugarden.persistence.cart;

import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.persistence.cart.entities.CartEntity;
import com.chis.trugarden.persistence.cart.entities.CartItemEntity;
import com.chis.trugarden.persistence.user.UserEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartEntityMapper {
    UserEntityMapper userEntityMapper = Mappers.getMapper(UserEntityMapper.class);
    CartItemEntityMapper cartItemEntityMapper = Mappers.getMapper(CartItemEntityMapper.class);

    default CartEntity toEntity(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(cart.getId());
        cartEntity.setUser(userEntityMapper.toEntity(cart.getUser()));
        cartEntity.setSessionId(cart.getSessionId());
        cartEntity.setTotalPrice(cart.getTotalPrice());
        cartEntity.setTotalMrpPrice(cart.getTotalMrpPrice());
        cartEntity.setQuantity(cart.getQuantity());
        cartEntity.setDiscount(cart.getDiscount());
        cartEntity.setStatus(cart.getStatus());
        cartEntity.setCouponCode(cart.getCouponCode());
        cartEntity.setCartItems(
                cart.getCartItems() != null
                        ? cart.getCartItems().stream()
                                .map(item -> {
                                    CartItemEntity itemEntity = cartItemEntityMapper.toEntity(item);
                                    itemEntity.setCart(cartEntity);
                                    return itemEntity;
                                })
                                .collect(Collectors.toSet())
                        : Set.of()
        );
        return cartEntity;
    }

    default Cart toDomain(CartEntity cartEntity) {
        if (cartEntity == null) {
            return null;
        }

        Set<CartItem> cartItems = cartEntity.getCartItems() != null
                ? cartEntity.getCartItems().stream()
                        .map(cartItemEntityMapper::toDomain)
                        .collect(Collectors.toSet())
                : Set.of();

        return Cart.of(
                cartEntity.getId(),
                userEntityMapper.toDomain(cartEntity.getUser()),
                cartEntity.getSessionId(),
                cartEntity.getTotalPrice(),
                cartEntity.getTotalMrpPrice(),
                cartEntity.getQuantity(),
                cartEntity.getDiscount(),
                cartEntity.getCouponCode(),
                cartEntity.getStatus(),
                cartItems
        );
    }
}
