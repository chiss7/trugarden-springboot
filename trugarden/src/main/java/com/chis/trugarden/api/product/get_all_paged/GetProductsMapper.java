package com.chis.trugarden.api.product.get_all_paged;

import com.chis.trugarden.application.product.get_all_paged.GetProductsQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GetProductsMapper {
    GetProductsQuery toQuery(GetProductsRequest request);
}
