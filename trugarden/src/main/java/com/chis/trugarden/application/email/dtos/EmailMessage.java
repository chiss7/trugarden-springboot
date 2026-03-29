package com.chis.trugarden.application.email.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailMessage(
        @Email @NotBlank String to,
        @NotBlank String subject,
        @NotBlank String htmlBody
) {}
