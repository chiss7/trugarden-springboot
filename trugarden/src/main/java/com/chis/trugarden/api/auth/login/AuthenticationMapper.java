package com.chis.trugarden.api.auth.login;

import com.chis.trugarden.application.auth.login.LoginCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    LoginCommand toLoginCommand(AuthenticationRequest request);
}
