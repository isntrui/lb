package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.services.UserService;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserService us;

    @Operation(summary = "Create new user")
    @PostMapping("create")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        us.register(user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user by id")
    @GetMapping("{id}")
    public ResponseEntity<User> getUserByPathId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(us.getUserById(id));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get user by email")
    @GetMapping("get")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(us.getUserByEmail(email));
    }

    @Operation(summary = "Change user's role")
    @PutMapping("{id}/changeRole")
    public ResponseEntity<Void> changeRoleByEmail(@PathVariable Long id, @RequestParam Role role) {
        String email = us.getUserById(id).getEmail();
        us.changeRole(email, role);
        return ResponseEntity.ok().build();
    }
}
