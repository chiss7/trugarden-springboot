package com.chis.trugarden.api.auth;

import com.chis.trugarden.api.auth.login.AuthenticationMapper;
import com.chis.trugarden.api.auth.login.AuthenticationRequest;
import com.chis.trugarden.api.auth.register.RegisterMapper;
import com.chis.trugarden.api.auth.register.RegistrationResponse;
import com.chis.trugarden.api.auth.register.RegistrationRequest;
import com.chis.trugarden.application.auth.activate.ActivationCommand;
import com.chis.trugarden.application.auth.login.LoginResult;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import com.chis.trugarden.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController extends ControllerBase {
    private final CommandGateway commandGateway;
    private final AuthenticationMapper authenticationMapper;
    private final RegisterMapper registerMapper;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<?>> register(@RequestBody @Valid RegistrationRequest request) {
        Result<Long> result = commandGateway.sendAndWait(registerMapper.toRegisterCommand(request));
        return result.isSuccess() ?
                created(result.getValue().toString(), new RegistrationResponse(result.getValue())) :
                error(result.getError());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<GenericResponse<?>> login(@RequestBody @Valid AuthenticationRequest request) {
        Result<LoginResult> result = commandGateway.sendAndWait(authenticationMapper.toLoginCommand(request));
        return result.isSuccess() ?
                success(authenticationMapper.toLoginResponse(result.getValue())) :
                error(result.getError());
    }

    @GetMapping("/activate")
    public ResponseEntity<GenericResponse<?>> activate(@RequestParam String token) {
        Result<Void> result = commandGateway.sendAndWait(new ActivationCommand(token));
        return result.isSuccess() ?
                success("Cuenta activada exitosamente.") :
                error(result.getError());
    }
}
