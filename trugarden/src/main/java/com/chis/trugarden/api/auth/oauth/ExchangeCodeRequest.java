package com.chis.trugarden.api.auth.oauth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExchangeCodeRequest {

    @NotBlank(message = "Code is required")
    private String code;
}
