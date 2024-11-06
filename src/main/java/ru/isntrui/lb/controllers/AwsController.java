package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.isntrui.lb.enums.FileType;
import ru.isntrui.lb.services.AwsService;
import ru.isntrui.lb.services.UserService;

import java.io.InputStream;

@RestController
@RequestMapping("/api/aws/")
@Tag(name = "AWS")
public class AwsController {
    private final UserService us;
    private final AwsService aws;

    @Autowired
    public AwsController(UserService us, AwsService aws) {
        this.us = us;
        this.aws = aws;
    }

    @Operation(summary = "Upload file to AWS")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam FileType type) {
        String n;
        String res;
        if (file.isEmpty() || type == null || file.getOriginalFilename() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            n = type + "_" + file.getOriginalFilename().hashCode() + "_" + us.getCurrentUser().getId().toString() + ((Math.random() * Math.random() * 10000) + "");
            InputStream is = file.getInputStream();
            res = aws.uploadFile(is, n, FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res);
    }
}
