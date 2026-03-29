package com.chis.trugarden.persistence.user;

import com.chis.trugarden.domain.user.Token;
import com.chis.trugarden.persistence.user.entities.TokenEntity;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TokenEntityMapper {
    UserEntityMapper userEntityMapper = Mappers.getMapper(UserEntityMapper.class);

    default TokenEntity toEntity(Token token) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setId(token.getId());
        tokenEntity.setToken(token.getToken());
        tokenEntity.setCreatedAt(token.getCreatedAt());
        tokenEntity.setExpiresAt(token.getExpiresAt());
        UserEntity userEntity = new UserEntity();
        userEntity.setId(token.getUser().getId());
        tokenEntity.setUserEntity(userEntity);
        tokenEntity.setValidatedAt(token.getValidatedAt());
        return tokenEntity;
    }

    default Token toDomain(TokenEntity tokenEntity) {
        return Token.of(
                tokenEntity.getId(),
                tokenEntity.getToken(),
                tokenEntity.getCreatedAt(),
                tokenEntity.getExpiresAt(),
                tokenEntity.getValidatedAt(),
                userEntityMapper.toDomain(tokenEntity.getUserEntity())
        );
    }

    default Token toShallowDomain(TokenEntity tokenEntity) {
        return Token.of(
                tokenEntity.getId(),
                tokenEntity.getToken(),
                tokenEntity.getCreatedAt(),
                tokenEntity.getExpiresAt(),
                tokenEntity.getValidatedAt(),
                null
        );
    }
}
