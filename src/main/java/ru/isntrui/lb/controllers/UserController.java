package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.exceptions.UnauthorizedException;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.services.InviteService;
import ru.isntrui.lb.services.UserService;

import java.util.Objects;

@Tag(name ="User")
@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserService us;
    @Autowired
    InviteService is;

    @Operation(summary = "Create new user")
    @PostMapping("create")
    public ResponseEntity<Void> createUser(
            @RequestBody @Parameter(description = "New use's object") User user,
            @RequestParam @Parameter(description = "Invite code, gotten from admin") String inviteCode) {
        if (is.findByCode(inviteCode) == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!Objects.equals(Objects.requireNonNull(is.findByCode(inviteCode)).getEmail(), user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user by id")
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable @Parameter(description = "User's id") Long id) {
        try {
            return ResponseEntity.ok(us.getUserById(id));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get user by email")
    @GetMapping("get")
    public ResponseEntity<User> getUserByEmail(@RequestParam @Parameter(description = "User's email") String email) {
        try {
            return ResponseEntity.ok(us.getUserByEmail(email));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Change user's role", description = "Available only for admins")
    @PutMapping("{id}/changeRole")
    public ResponseEntity<Void> changeRoleByEmail(
            @PathVariable @Parameter(description = "User's id") Long id,
            @RequestParam @Parameter(description = "New role") Role role
    ) {
        String email;
        try {
            email = us.getUserById(id).getEmail();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        us.changeRole(email, role);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Change user's password")
    @PutMapping("{id}/changePassword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "200", description = "Password changed")
    })
    public ResponseEntity<Void> changePassword(
            @PathVariable @Parameter(description = "User's id") Long id,
            @RequestParam @Parameter(description = "Crypt old password") String oldPassword,
            @RequestParam @Parameter(description = "Crypt new password") String newPassword) {
        try {
            us.changePassword(id, oldPassword, newPassword);
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(401).body(null);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user by id", description = "Available only for admins")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeUserById(@PathVariable @Parameter(description = "User's id") Long id) {
        us.remove(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user by email", description = "Available only for admins")
    @DeleteMapping("remove")
    public ResponseEntity<Void> removeUserByEmail(@RequestParam @Parameter(description = "User's email") String email) {
        us.remove(email);
        return ResponseEntity.ok().build();
    }
}
