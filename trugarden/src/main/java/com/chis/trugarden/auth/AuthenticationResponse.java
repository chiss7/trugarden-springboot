package com.chis.trugarden.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
}
