package ru.isntrui.lb.controllers;

import cn.hutool.core.date.DateTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isntrui.lb.enums.FileType;
import ru.isntrui.lb.services.AwsService;
import ru.isntrui.lb.services.UserService;

import java.io.InputStream;
import java.util.Objects;

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
    public ResponseEntity<String> upload(@RequestPart @Parameter(description = "File with content to upload") MultipartFile file, @RequestParam @Parameter(description = "Type of content") FileType type) {
        String n;
        String res;
        try {
            n = type.toString() + "_" + Objects.requireNonNull(file.getOriginalFilename()).hashCode() + "_" + us.getCurrentUser().getId() + "-" + DateTime.now().toString("yyyy-MM-dd_HH-mm-ss");
        } catch (Exception e) {
            System.out.println("Error 1: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        try {
            InputStream is = file.getInputStream();
            res = aws.uploadFile(is, n, FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (Exception e) {
            System.out.println("Error 2: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res);
    }
}
