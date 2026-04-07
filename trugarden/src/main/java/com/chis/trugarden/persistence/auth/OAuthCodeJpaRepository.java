package com.chis.trugarden.persistence.auth;

import com.chis.trugarden.persistence.auth.entities.OAuthCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OAuthCodeJpaRepository extends JpaRepository<OAuthCodeEntity, String> {

    /**
     * Finds a valid (unused and not expired) OAuth code.
     *
     * @param code the authorization code
     * @param now current timestamp
     * @return optional OAuth code entity
     */
    Optional<OAuthCodeEntity> findByCodeAndUsedFalseAndExpiresAtAfter(String code, LocalDateTime now);

    /**
     * Deletes all expired OAuth codes.
     *
     * @param now current timestamp
     */
    void deleteByExpiresAtBefore(LocalDateTime now);

    @Modifying
    @Query("UPDATE OAuthCodeEntity o SET o.used = true WHERE o.code = :code AND o.used = false AND o.expiresAt > :now")
    int markAsUsed(@Param("code") String code, @Param("now") LocalDateTime now);
}
