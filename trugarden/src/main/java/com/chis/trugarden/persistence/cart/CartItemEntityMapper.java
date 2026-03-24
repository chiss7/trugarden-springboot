package com.chis.trugarden.persistence.cart;

import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.persistence.cart.entities.CartItemEntity;
import com.chis.trugarden.persistence.product.ProductEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartItemEntityMapper {
    ProductEntityMapper productEntityMapper = Mappers.getMapper(ProductEntityMapper.class);

    default CartItemEntity toEntity(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(cartItem.getId());
        cartItemEntity.setProduct(productEntityMapper.toEntity(cartItem.getProduct()));
        cartItemEntity.setQuantity(cartItem.getQuantity());
        cartItemEntity.setMrpPrice(cartItem.getMrpPrice());
        cartItemEntity.setSellingPrice(cartItem.getSellingPrice());
        cartItemEntity.setUserId(cartItem.getUserId());
        return cartItemEntity;
    }

    default CartItem toDomain(CartItemEntity cartItemEntity) {
        if (cartItemEntity == null) {
            return null;
        }

        return CartItem.of(
                cartItemEntity.getId(),
                cartItemEntity.getCart().getId(),
                productEntityMapper.toDomain(cartItemEntity.getProduct()),
                cartItemEntity.getQuantity(),
                cartItemEntity.getMrpPrice(),
                cartItemEntity.getSellingPrice(),
                cartItemEntity.getUserId()
        );
    }
}
