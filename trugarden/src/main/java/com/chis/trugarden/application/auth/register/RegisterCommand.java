package com.chis.trugarden.application.auth.register;

public record RegisterCommand(
        String firstname,
        String lastname,
        String email,
        String password,
        String confirmPassword
) {
}
