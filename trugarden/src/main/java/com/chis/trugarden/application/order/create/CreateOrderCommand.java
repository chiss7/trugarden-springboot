package com.chis.trugarden.application.order.create;

public record CreateOrderCommand(
        Long shippingAddressId,
        String principalStreet,
        String secondaryStreet,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String houseNumber,
        String zipCode,
        String sector,
        String city
) {
}
