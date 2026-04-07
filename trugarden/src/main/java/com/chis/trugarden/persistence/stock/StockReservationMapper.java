package com.chis.trugarden.persistence.stock;

import com.chis.trugarden.domain.stock.StockReservation;
import com.chis.trugarden.persistence.stock.entities.StockReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockReservationMapper {

    @Mapping(target = "createdAt", source = "createdDate")
    StockReservation toDomain(StockReservationEntity entity);

    @Mapping(target = "createdDate", source = "createdAt")
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    StockReservationEntity toEntity(StockReservation domain);
}
