package com.chis.trugarden.persistence.order;

import com.chis.trugarden.domain.order.OrderItem;
import com.chis.trugarden.persistence.order.entities.OrderItemEntity;
import com.chis.trugarden.persistence.product.ProductEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemEntityMapper {
    ProductEntityMapper productEntityMapper = Mappers.getMapper(ProductEntityMapper.class);

    default OrderItemEntity toEntity(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(orderItem.getId());
        entity.setProduct(productEntityMapper.toEntity(orderItem.getProduct()));
        entity.setQuantity(orderItem.getQuantity());
        entity.setOriginalPrice(orderItem.getOriginalPrice());
        entity.setSubtotal(orderItem.getSubtotal());
        entity.setUnitPrice(orderItem.getUnitPrice());
        entity.setTaxPercentage(orderItem.getTaxPercentage());
        entity.setTaxAmount(orderItem.getTaxAmount());
        entity.setUserId(orderItem.getUserId());
        entity.setPromisedLeadTimeMinDays(orderItem.getPromisedLeadTimeMinDays());
        entity.setPromisedLeadTimeMaxDays(orderItem.getPromisedLeadTimeMaxDays());

        return entity;
    }

    default OrderItem toDomain(OrderItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return OrderItem.of(
                entity.getId(),
                entity.getOrder().getId(),
                productEntityMapper.toDomain(entity.getProduct()),
                entity.getQuantity(),
                entity.getOriginalPrice(),
                entity.getUnitPrice(),
                entity.getSubtotal(),
                entity.getTaxPercentage(),
                entity.getTaxAmount(),
                entity.getUserId(),
                entity.getPromisedLeadTimeMinDays(),
                entity.getPromisedLeadTimeMaxDays()
        );
    }
}
