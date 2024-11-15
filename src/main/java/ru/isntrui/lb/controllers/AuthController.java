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
import ru.isntrui.lb.services.InviteService;
import ru.isntrui.lb.services.SEmailService;
import ru.isntrui.lb.services.UserService;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final InviteService inviteService;
    private final SEmailService emailService;
    @Operation(summary = "Register user")
    @PostMapping("sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody @Valid @Parameter(description = "Formatted request with credentials") SignUpRequest request) {
        try {
            JwtAuthenticationResponse r = authenticationService.signUp(request);
            /*emailService.sendSimpleEmail(
                    request.getEmail(),
                    "Регистрация на LB Tool",
                    request.getFirstName() + ", добро пожаловать на платформу LB Tool! С этой почты будут приходить уведомления о новых задачах! \nТвой логин: " + request.getUsername()
            );*/
            return ResponseEntity.ok().body(r);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Auth user")
    @PostMapping("sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid @Parameter(description = "Formatted request with credentials") SignInRequest request) {
        System.out.println(request.getUsername() + " " + request.getPassword());
        try {
            return ResponseEntity.ok().body(authenticationService.signIn(request));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Change password")
    @PutMapping("changePassword")
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

    @Operation(summary = "Is email taken?")
    @GetMapping("check/email")
    public ResponseEntity<Boolean> isEmailTaken(@RequestParam @Parameter(description = "Email to check") String email) {
        return ResponseEntity.ok(userService.isEmailTaken(email));
    }

    @Operation(summary = "Is username taken?")
    @GetMapping("check/username")
    public ResponseEntity<Boolean> isUsernameTaken(@RequestParam @Parameter(description = "Email to check") String username) {
        return ResponseEntity.ok(userService.isUsernameTaken(username));
    }

    @Operation(summary = "Is invite code valid?")
    @GetMapping("check/invite")
    public ResponseEntity<Boolean> isInviteValid(@RequestParam @Parameter(description = "Invite code to check") String code) {
        return ResponseEntity.ok(inviteService.findByCode(code) != null);
    }
}
