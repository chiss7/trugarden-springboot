package com.chis.trugarden.persistence.auth;

import com.chis.trugarden.domain.auth.RefreshToken;
import com.chis.trugarden.persistence.auth.entities.RefreshTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenEntityMapper {
    default RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(refreshToken.getId());
        entity.setUserId(refreshToken.getUserId());
        entity.setFamilyId(refreshToken.getFamilyId());
        entity.setTokenHash(refreshToken.getTokenHash());
        entity.setCreatedAt(refreshToken.getCreatedAt());
        entity.setExpiresAt(refreshToken.getExpiresAt());
        entity.setRevokedAt(refreshToken.getRevokedAt());
        entity.setReplacedByTokenHash(refreshToken.getReplacedByTokenHash());
        return entity;
    }

    default RefreshToken toDomain(RefreshTokenEntity entity) {
        return RefreshToken.of(
                entity.getId(),
                entity.getUserId(),
                entity.getFamilyId(),
                entity.getTokenHash(),
                entity.getCreatedAt(),
                entity.getExpiresAt(),
                entity.getRevokedAt(),
                entity.getReplacedByTokenHash()
        );
    }
}
