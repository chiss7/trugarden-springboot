package com.chis.trugarden.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotEmpty(message = "Firstname is required")
        @NotBlank(message = "Firstname is required")
        String firstname,
        @NotEmpty(message = "Lastname is required")
        @NotBlank(message = "Lastname is required")
        String lastname,
        @Email(message = "Email is not well formatted")
        @NotEmpty(message = "Email is required")
        @NotBlank(message = "Email is required")
        String email,
        @Size(min = 8, message = "Password should be 8 characters at least")
        @NotEmpty(message = "Password is required")
        @NotBlank(message = "Password is required")
        String password,
        @Size(min = 8, message = "Password should be 8 characters at least")
        @NotEmpty(message = "Confirm Password is required")
        @NotBlank(message = "Confirm Password is required")
        String confirmPassword
) {
}
