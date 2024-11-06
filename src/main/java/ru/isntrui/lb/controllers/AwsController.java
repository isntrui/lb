package ru.isntrui.lb.controllers;

import cn.hutool.core.date.DateTime;
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

import java.io.IOException;
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
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam FileType type) throws IOException {
        String n;
        String res;
        if (file.isEmpty() || type == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            n = type + "_" + Objects.requireNonNull(file.getOriginalFilename()).hashCode() + "_" + us.getCurrentUser().getId() + "-" + DateTime.now().toString("yyyy-MM-dd_HH-mm-ss");
            InputStream is = file.getInputStream();
            res = aws.uploadFile(is, n, FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res);
    }
}
