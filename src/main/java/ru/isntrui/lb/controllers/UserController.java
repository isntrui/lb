package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.exceptions.UnauthorizedException;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.services.InviteService;
import ru.isntrui.lb.services.UserService;

import java.util.Objects;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserService us;
    @Autowired
    InviteService is;

    @Operation(summary = "Create new user")
    @PostMapping("create")
    public ResponseEntity<Void> createUser(@RequestBody User user, @RequestParam String inviteCode) {
        if (is.findByCode(inviteCode) == null) {
            return ResponseEntity.badRequest().build();
        }
        if (us.checkTg(user.getTgUsername())) {
            return ResponseEntity.badRequest().build();
        }
        if (!Objects.equals(Objects.requireNonNull(is.findByCode(inviteCode)).getEmail(), user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        us.register(user, inviteCode);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user by id")
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(us.getUserById(id));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get user by email")
    @GetMapping("get")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        try {
            return ResponseEntity.ok(us.getUserByEmail(email));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Change user's role")
    @PutMapping("{id}/changeRole")
    public ResponseEntity<Void> changeRoleByEmail(@PathVariable Long id, @RequestParam Role role) {
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
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        try {
            us.changePassword(id, oldPassword, newPassword);
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(401).body(null);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user by id")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeUserById(@PathVariable Long id) {
        us.remove(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user by email")
    @DeleteMapping("remove")
    public ResponseEntity<Void> removeUserByEmail(@RequestParam String email) {
        us.remove(email);
        return ResponseEntity.ok().build();
    }
}
