package ru.isntrui.lb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.services.UserService;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    UserService us;
    @PostMapping("create")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        us.register(user);
        return ResponseEntity.ok().build();
    }


}
