package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.services.UserService;

import java.util.List;

@Tag(name ="User")
@RestController
@RequestMapping("/api/user/")
public class UserController {
    private final UserService us;

    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }

    @Operation(summary = "Set user's avatar")
    @PutMapping("/setAvatar")
    public ResponseEntity<Void> setAvatar(
            @RequestParam @Parameter(description = "Avatar's url") String url
    ) {
        us.setAvatar(us.getCurrentUser().getId(), url);
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

    @Operation(summary = "Get all users by role", description = "Available only for admins")
    @GetMapping("getAllByRole")
    public ResponseEntity<List<User>> getAllByRole(@RequestParam @Parameter(description = "Role") Role role) {
        if (us.getCurrentUser().getRole() != Role.ADMIN && us.getCurrentUser().getRole() != Role.HEAD && us.getCurrentUser().getRole() != Role.COORDINATOR) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(us.getAllByRole(role));
    }

    @Operation(summary = "Get all users")
    @GetMapping("all")
    public ResponseEntity<List<User>> getAll() {
        if (us.getCurrentUser().getRole() != Role.ADMIN && us.getCurrentUser().getRole() != Role.HEAD && us.getCurrentUser().getRole() != Role.COORDINATOR) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(us.getAll());
    }
}
