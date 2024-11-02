package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
import ru.isntrui.lb.services.UserService;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(summary = "Register user")
    @PostMapping("sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody @Valid @Parameter(description = "Formatted request with credentials") SignUpRequest request, HttpServletRequest req) {
        try {
            return ResponseEntity.ok().body(authenticationService.signUp(request, req.getRemoteAddr()));
        } catch (Exception ex) {
            if (ex.getMessage().equals("Приглашение не найдено")) {
                return ResponseEntity.status(404).build();
            } else if (ex.getMessage().equals("Email не совпадает с приглашением")) {
                return ResponseEntity.status(401).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @Operation(summary = "Is username taken?")
    @GetMapping("check/username")
    public ResponseEntity<Void> isUNameTaken(@RequestParam String username) {
        try {
            userService.getByUsername(username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Is email taken?")
    @GetMapping("check/email")
    public ResponseEntity<Void> isEmailTaken(@RequestParam String email) {
        try {
            userService.getUserByEmail(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Auth user")
    @PostMapping("sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid @Parameter(description = "Formatted request with credentials") SignInRequest request, HttpServletRequest req) {
        try {
            return ResponseEntity.ok().body(authenticationService.signIn(request, req.getRemoteAddr()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }    }

    @Operation(summary = "Change password")
    @PutMapping("changePassword")
    @PostMapping("change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest cp, HttpServletRequest req) {
        try {
            authenticationService.changePassword(cp, req.getRemoteAddr());
            return ResponseEntity.ok("Пароль успешно изменён");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
