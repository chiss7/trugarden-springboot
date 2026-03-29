package com.chis.trugarden.persistence.user.repositories;

import com.chis.trugarden.application.user.abstractions.AddressRepository;
import com.chis.trugarden.domain.user.Address;
import com.chis.trugarden.persistence.user.AddressEntityMapper;
import com.chis.trugarden.persistence.user.AddressJpaRepository;
import com.chis.trugarden.persistence.user.entities.AddressEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the AddressRepository for managing Address entities in the database.
 * This repository is specifically for handling addresses associated with guest users,
 * as registered users will have their addresses managed through the User entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {
    private final AddressEntityMapper addressEntityMapper;
    private final AddressJpaRepository addressJpaRepository;

    @Override
    public Address save(Address address) {
        AddressEntity entity = addressEntityMapper.toEntity(address);
        return addressEntityMapper.toDomain(addressJpaRepository.save(entity));
    }

    @Override
    public Optional<Address> findByIdAndSessionId(Long id, String sessionId) {
        return addressJpaRepository.findByIdAndSessionId(id, sessionId)
                .map(addressEntityMapper::toDomain)
                .or(() -> {
                    log.warn("Address with id {} and sessionId {} not found", id, sessionId);
                    return Optional.empty();
                });
    }
}
