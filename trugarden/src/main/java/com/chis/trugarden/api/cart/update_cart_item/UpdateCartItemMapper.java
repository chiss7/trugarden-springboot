package com.chis.trugarden.api.cart.update_cart_item;

import com.chis.trugarden.application.cart.update_cart_item.UpdateCartItemCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateCartItemMapper {
    UpdateCartItemCommand toCommand(UpdateCartItemRequest request);
}
