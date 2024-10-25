package ru.isntrui.lb.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReqbinController {

    @GetMapping("/reqbin-verify.txt")
    public ResponseEntity<String> getTextFile() {
        // Set the Content-Type header to "text/plain"
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        // Return an empty body with status 200 OK and text/plain Content-Type
        return new ResponseEntity<>("", headers, HttpStatus.OK);
    }
}
