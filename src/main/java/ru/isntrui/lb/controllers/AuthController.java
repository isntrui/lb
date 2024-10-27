package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isntrui.lb.queries.JwtAuthenticationResponse;
import ru.isntrui.lb.queries.SignInRequest;
import ru.isntrui.lb.queries.SignUpRequest;
import ru.isntrui.lb.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register user")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid @Parameter(description = "Formatted request with credentials") SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Auth user")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid @Parameter(description = "Formatted request with credentials") SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
