package com.chis.trugarden.persistence.user;

import com.chis.trugarden.persistence.user.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByIdAndSessionId(Long id, String sessionId);
}
