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
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register user")
    @PostMapping("sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody @Valid @Parameter(description = "Formatted request with credentials") SignUpRequest request) {
        try {
            return ResponseEntity.ok().body(authenticationService.signUp(request));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Auth user")
    @PostMapping("sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid @Parameter(description = "Formatted request with credentials") SignInRequest request) {
        try {
            return ResponseEntity.ok().body(authenticationService.signIn(request));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }    }

    @Operation(summary = "Change password")
    @PutMapping("changePassword")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest cp) {
        try {
            authenticationService.changePassword(cp);
            return ResponseEntity.ok("Пароль успешно изменён");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
