package com.chis.trugarden.application.auth.get_current_user;

public record GetCurrentUserResult(
        Long id,
        String fullName,
        String email,
        String image
) {
}
