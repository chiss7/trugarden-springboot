package com.chis.trugarden.api.auth.register;

import com.chis.trugarden.application.auth.register.RegisterCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    RegisterCommand toRegisterCommand(RegistrationRequest request);
}
