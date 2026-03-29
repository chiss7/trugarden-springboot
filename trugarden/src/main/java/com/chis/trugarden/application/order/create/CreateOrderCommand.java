package com.chis.trugarden.application.order.create;

public record CreateOrderCommand(
        String sessionId,
        Long shippingAddressId,
        String principalStreet,
        String secondaryStreet,
        String houseNumber,
        String zipCode,
        String sector,
        String city
) {
}
