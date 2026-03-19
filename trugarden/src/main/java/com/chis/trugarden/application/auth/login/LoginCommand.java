package com.chis.trugarden.application.auth.login;

public record LoginCommand(
        String email,
        String password
) {
}
