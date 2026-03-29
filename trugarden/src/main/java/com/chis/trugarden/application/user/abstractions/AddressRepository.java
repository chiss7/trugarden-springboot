package com.chis.trugarden.application.user.abstractions;

import com.chis.trugarden.domain.user.Address;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Address entities.
 * Only for those address for guest users,
 * as registered users will have their addresses managed through the User entity.
 */
@Repository
public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findByIdAndSessionId(Long id, String sessionId);
}
