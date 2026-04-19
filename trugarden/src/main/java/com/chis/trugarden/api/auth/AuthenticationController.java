package com.chis.trugarden.api.auth;

import com.chis.trugarden.api.auth.login.AuthenticationMapper;
import com.chis.trugarden.api.auth.login.AuthenticationRequest;
import com.chis.trugarden.api.auth.oauth.ExchangeCodeMapper;
import com.chis.trugarden.api.auth.oauth.ExchangeCodeRequest;
import com.chis.trugarden.api.auth.register.RegisterMapper;
import com.chis.trugarden.api.auth.register.RegistrationResponse;
import com.chis.trugarden.api.auth.register.RegistrationRequest;
import com.chis.trugarden.application.auth.activate.ActivationCommand;
import com.chis.trugarden.application.auth.get_current_user.GetCurrentUserQuery;
import com.chis.trugarden.application.auth.get_current_user.GetCurrentUserQueryResult;
import com.chis.trugarden.application.auth.logout.LogoutCommand;
import com.chis.trugarden.application.auth.refresh.RefreshTokenCommand;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import com.chis.trugarden.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController extends ControllerBase {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final AuthenticationMapper authenticationMapper;
    private final RegisterMapper registerMapper;
    private final ExchangeCodeMapper exchangeCodeMapper;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<?>> register(@RequestBody @Valid RegistrationRequest request) {
        Result<Long> result = commandGateway.sendAndWait(registerMapper.toRegisterCommand(request));
        return result.isSuccess() ?
                created(result.getValue().toString(), new RegistrationResponse(result.getValue())) :
                error(result.getError());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<GenericResponse<?>> login(@RequestBody @Valid AuthenticationRequest request) {
        Result<Void> result = commandGateway.sendAndWait(authenticationMapper.toLoginCommand(request));
        return result.isSuccess() ?
                success("Inicio de sesión exitoso") :
                error(result.getError());
    }

    @GetMapping("/activate")
    public ResponseEntity<GenericResponse<?>> activate(@RequestParam String token) {
        Result<Void> result = commandGateway.sendAndWait(new ActivationCommand(token));
        return result.isSuccess() ?
                success("Cuenta activada exitosamente.") :
                error(result.getError());
    }

    @PostMapping("/oauth/exchange")
    public ResponseEntity<GenericResponse<?>> exchangeOAuthCode(
            @RequestBody @Valid ExchangeCodeRequest request
    ) {
        Result<Void> result = commandGateway.sendAndWait(exchangeCodeMapper.toCommand(request));
        return result.isSuccess() ?
                success("Token intercambiado exitosamente.") :
                error(result.getError());
    }

    @PostMapping("/refresh")
    public ResponseEntity<GenericResponse<?>> refresh() {
        Result<Void> result = commandGateway.sendAndWait(new RefreshTokenCommand());
        return result.isSuccess() ?
                success("Token refrescado exitosamente.") :
                error(result.getError());
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<?>> logout() {
        Result<Void> result = commandGateway.sendAndWait(new LogoutCommand());
        return result.isSuccess() ?
                success("Sesion cerrada exitosamente.") :
                error(result.getError());
    }

    @GetMapping("/me")
    public ResponseEntity<GenericResponse<?>> getCurrentUser() {
        GetCurrentUserQueryResult result = queryGateway.query(new GetCurrentUserQuery(), GetCurrentUserQueryResult.class).join();
        return result.isSuccess() ?
                success(result.getValue()) :
                error(result.getError());
    }
}
