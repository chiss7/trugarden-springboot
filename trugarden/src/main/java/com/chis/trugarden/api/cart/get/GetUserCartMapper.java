package com.chis.trugarden.api.cart.get;

import com.chis.trugarden.application.cart.get.GetUserCartResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GetUserCartMapper {
    GetUserCartResponse toResponse(GetUserCartResult result);
}
