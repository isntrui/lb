package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.queries.ChangePasswordRequest;
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

    @Operation(summary = "Change password")
    @PutMapping("changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid @Parameter(description = "Formatted request with new password") ChangePasswordRequest request) {
        try {
            authenticationService.changePassword(request);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
