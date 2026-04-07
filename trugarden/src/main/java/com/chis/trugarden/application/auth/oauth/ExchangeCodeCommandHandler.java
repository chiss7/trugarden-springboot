package com.chis.trugarden.application.auth.oauth;

import com.chis.trugarden.application.auth.service.OAuthCodeService;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import com.chis.trugarden.shared.result.Result;
import com.chis.trugarden.shared.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeCodeCommandHandler {
    private final OAuthCodeService oAuthCodeService;
    private final JwtService jwtService;

    @CommandHandler
    public Result<ExchangeCodeResult> handle(ExchangeCodeCommand command){
        Result<User> userResult = oAuthCodeService.exchangeCode(command.code());

        if (userResult.isFailure()) {
            return Result.failure(userResult.getError());
        }

        User user = userResult.getValue();
        CustomUserDetails userDetails = new CustomUserDetails(user, null);
        String jwtToken = jwtService.generateToken(userDetails);

        return Result.success(new ExchangeCodeResult(jwtToken));
    }
}
