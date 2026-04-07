package com.chis.trugarden.api.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeCodeResponse {
    private String token;
    private String email;
    private String fullName;
}
