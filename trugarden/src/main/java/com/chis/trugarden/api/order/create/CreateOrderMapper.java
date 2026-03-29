package com.chis.trugarden.api.order.create;

import com.chis.trugarden.application.order.create.CreateOrderCommand;
import com.chis.trugarden.application.order.create.CreateOrderResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateOrderMapper {
    CreateOrderCommand toCommand(CreateOrderRequest request);
    CreateOrderResponse toResponse(CreateOrderResult result);
}
