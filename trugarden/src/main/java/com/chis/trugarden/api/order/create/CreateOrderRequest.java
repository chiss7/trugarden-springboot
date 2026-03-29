package com.chis.trugarden.api.order.create;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
    private String sessionId;

    private Long shippingAddressId;
    private String principalStreet;
    private String secondaryStreet;
    private String houseNumber;
    private String zipCode;
    private String sector;
    private String city;

    @AssertTrue(message = "Debes completar todos los campos de la dirección.")
    public boolean isAddressValid() {

        if (shippingAddressId != null) {
            return true;
        }

        return notBlank(principalStreet) &&
                notBlank(secondaryStreet) &&
                notBlank(houseNumber) &&
                notBlank(zipCode) &&
                notBlank(sector) &&
                notBlank(city);
    }

    private boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
