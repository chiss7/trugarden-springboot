package com.chis.trugarden.persistence.order;

import com.chis.trugarden.domain.order.Order;
import com.chis.trugarden.persistence.cart.entities.CartItemEntity;
import com.chis.trugarden.persistence.order.entities.OrderEntity;
import com.chis.trugarden.persistence.order.entities.OrderItemEntity;
import com.chis.trugarden.persistence.order.entities.PaymentOrderEntity;
import com.chis.trugarden.persistence.user.AddressEntityMapper;
import com.chis.trugarden.persistence.user.UserEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {
    OrderItemEntityMapper orderItemEntityMapper = Mappers.getMapper(OrderItemEntityMapper.class);
    UserEntityMapper userEntityMapper = Mappers.getMapper(UserEntityMapper.class);
    AddressEntityMapper addressEntityMapper = Mappers.getMapper(AddressEntityMapper.class);
    PaymentOrderEntityMapper paymentOrderEntityMapper = Mappers.getMapper(PaymentOrderEntityMapper.class);

    default OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setOrderId(order.getOrderId());
        entity.setUser(userEntityMapper.toEntity(order.getUser()));
        entity.setSessionId(order.getSessionId());
        entity.setShippingAddress(addressEntityMapper.toEntity(order.getShippingAddress()));
        entity.setSubtotal(order.getSubtotal());
        entity.setTotalTax(order.getTotalTax());
        entity.setDiscountPercentage(order.getDiscountPercentage());
        entity.setCouponDiscountAmount(order.getCouponDiscountAmount());
        entity.setGrandTotal(order.getGrandTotal());
        entity.setOrderStatus(order.getOrderStatus());
        entity.setOrderDate(order.getOrderDate());
        entity.setDeliveryDate(order.getDeliveryDate());

        entity.setOrderItems(
                order.getOrderItems() != null
                        ? order.getOrderItems().stream()
                            .map(item -> {
                                OrderItemEntity itemEntity = orderItemEntityMapper.toEntity(item);
                                itemEntity.setOrder(entity);
                                return itemEntity;
                            })
                            .collect(Collectors.toList())
                        : List.of()
        );

        if (order.getPayment() != null) {
            PaymentOrderEntity paymentEntity = paymentOrderEntityMapper.toEntity(order.getPayment());
            paymentEntity.setOrder(entity);
            entity.setPayment(paymentEntity);
        }

        return entity;
    }

    default Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        return Order.of(
                entity.getId(),
                entity.getOrderId(),
                userEntityMapper.toDomain(entity.getUser()),
                entity.getSessionId(),
                entity.getSubtotal(),
                entity.getTotalTax(),
                entity.getDiscountPercentage(),
                entity.getCouponDiscountAmount(),
                entity.getGrandTotal(),
                entity.getOrderStatus(),
                entity.getOrderDate(),
                entity.getDeliveryDate(),
                entity.getOrderItems() != null ? entity.getOrderItems().stream()
                        .map(orderItemEntityMapper::toDomain)
                        .collect(Collectors.toList()) : null,
                addressEntityMapper.toDomain(entity.getShippingAddress()),
                paymentOrderEntityMapper.toDomain(entity.getPayment())
        );
    }
}
