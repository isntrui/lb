package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.exceptions.UnauthorizedException;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.services.UserService;

@Tag(name ="User")
@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserService us;

    @Operation(summary = "Get user by id")
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable @Parameter(description = "User's id") Long id) {
        try {
            return ResponseEntity.ok(us.getUserById(id));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get current user")
    @GetMapping
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(us.getCurrentUser());
    }

    @Operation(summary = "Get user by email")
    @GetMapping("get")
    public ResponseEntity<User> getUserByEmail(@RequestParam @Parameter(description = "User's email") String email) {
        if (us.getCurrentUser().getRole() != Role.ADMIN && us.getCurrentUser().getRole() != Role.HEAD && us.getCurrentUser().getRole() != Role.COORDINATOR) {
            return ResponseEntity.status(403).build();
        }
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
        if (us.getCurrentUser().getRole() != Role.HEAD && us.getCurrentUser().getRole() != Role.ADMIN && us.getCurrentUser().getRole() != Role.COORDINATOR) {
            return ResponseEntity.status(403).build();
        }
        if (us.getCurrentUser().getRole() == Role.HEAD && role == Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        if (us.getUserById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (us.getUserById(id).getRole() == Role.HEAD && us.getCurrentUser().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }
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
            @RequestParam @Parameter(description = "Crypt old password") String oldPassword,
            @RequestParam @Parameter(description = "Crypt new password") String newPassword) {
        try {
            us.changePassword(us.getCurrentUser().getId(), oldPassword, newPassword);
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
        if (us.getUserById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (us.getCurrentUser().getRole() != Role.ADMIN && us.getCurrentUser().getRole() != Role.HEAD && us.getCurrentUser().getRole() != Role.COORDINATOR) {
            return ResponseEntity.status(403).build();
        }
        if (us.getUserById(id).getRole() == Role.HEAD && us.getCurrentUser().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        us.remove(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user by email", description = "Available only for admins")
    @DeleteMapping("remove")
    public ResponseEntity<Void> removeUserByEmail(@RequestParam @Parameter(description = "User's email") String email) {
        if (us.getUserByEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        if (us.getCurrentUser().getRole() != Role.ADMIN && us.getCurrentUser().getRole() != Role.HEAD && us.getCurrentUser().getRole() != Role.COORDINATOR) {
            return ResponseEntity.status(403).build();
        }
        if (us.getUserByEmail(email).getRole() == Role.HEAD && us.getCurrentUser().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        us.remove(email);
        return ResponseEntity.ok().build();
    }
}
