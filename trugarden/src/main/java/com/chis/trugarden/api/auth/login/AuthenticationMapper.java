package com.chis.trugarden.api.auth.login;

import com.chis.trugarden.application.auth.login.LoginCommand;
import com.chis.trugarden.application.auth.login.LoginResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    LoginCommand toLoginCommand(AuthenticationRequest request);
    AuthenticationResponse toLoginResponse(LoginResult loginResult);
}
