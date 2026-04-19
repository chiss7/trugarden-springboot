package com.chis.trugarden.api.auth.oauth;

import com.chis.trugarden.application.auth.oauth.ExchangeCodeCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeCodeMapper {
    ExchangeCodeCommand toCommand(ExchangeCodeRequest request);
}
