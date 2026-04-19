package com.chis.trugarden.application.auth.get_current_user;

import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.infrastructure.security.AuthenticationHelper;
import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentUserQueryHandler {
    @QueryHandler
    public GetCurrentUserQueryResult handle(GetCurrentUserQuery query) {
        CustomUserDetails user = AuthenticationHelper.getCurrentUser();
        User domainUser = user.getDomainUser();
        return GetCurrentUserQueryResult.success(
                new GetCurrentUserResult(
                        domainUser.getId(),
                        domainUser.getFullName(),
                        domainUser.getEmail().value(),
                        domainUser.getImage()
                )
        );
    }
}
