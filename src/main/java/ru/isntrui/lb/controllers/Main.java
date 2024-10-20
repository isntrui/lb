package ru.isntrui.lb.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Main {
    @GetMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("ok");
    }
}
