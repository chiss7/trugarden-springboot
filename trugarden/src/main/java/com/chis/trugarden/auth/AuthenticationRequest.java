package com.chis.trugarden.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @Email(message = "Email is not well formatted")
        @NotEmpty(message = "Email is required")
        @NotBlank(message = "Email is required")
        String email,
        @Size(min = 8, message = "Password should be 8 characters long minimum")
        @NotEmpty(message = "Password is required")
        @NotBlank(message = "Password is required")
        String password
) {
}
