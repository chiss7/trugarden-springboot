package com.chis.trugarden.persistence.user;

import com.chis.trugarden.domain.user.Address;
import com.chis.trugarden.persistence.user.entities.AddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressEntityMapper {
    default AddressEntity toEntity(Address address) {
        if (address == null) {
            return null;
        }

        AddressEntity entity = new AddressEntity();
        entity.setId(address.getId());
        entity.setPrincipalStreet(address.getPrincipalStreet());
        entity.setSecondaryStreet(address.getSecondaryStreet());
        entity.setFirstName(address.getFirstName());
        entity.setLastName(address.getLastName());
        entity.setEmail(address.getEmail());
        entity.setPhoneNumber(address.getPhoneNumber());
        entity.setHouseNumber(address.getHouseNumber());
        entity.setCity(address.getCity());
        entity.setSector(address.getSector());
        entity.setZipCode(address.getZipCode());
        entity.setSessionId(address.getSessionId());
        return entity;
    }

    default Address toDomain(AddressEntity entity) {
        if (entity == null) {
            return null;
        }

        return Address.of(
                entity.getId(),
                entity.getPrincipalStreet(),
                entity.getSecondaryStreet(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getHouseNumber(),
                entity.getZipCode(),
                entity.getSector(),
                entity.getCity(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getSessionId()
        );
    }
}
