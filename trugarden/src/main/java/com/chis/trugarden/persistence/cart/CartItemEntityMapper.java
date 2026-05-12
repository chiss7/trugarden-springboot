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
        cartItemEntity.setOriginalPrice(cartItem.getOriginalPrice());
        cartItemEntity.setUnitPrice(cartItem.getUnitPrice());
        cartItemEntity.setSubtotal(cartItem.getSubtotal());
        cartItemEntity.setTaxPercentage(cartItem.getTaxPercentage());
        cartItemEntity.setTaxAmount(cartItem.getTaxAmount());
        cartItemEntity.setUserId(cartItem.getUserId());
        cartItemEntity.setLeadTimeMinDays(cartItem.getLeadTimeMinDays());
        cartItemEntity.setLeadTimeMaxDays(cartItem.getLeadTimeMaxDays());
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
                cartItemEntity.getOriginalPrice(),
                cartItemEntity.getUnitPrice(),
                cartItemEntity.getTaxPercentage(),
                cartItemEntity.getUserId(),
                cartItemEntity.getLeadTimeMinDays(),
                cartItemEntity.getLeadTimeMaxDays()
        );
    }
}
