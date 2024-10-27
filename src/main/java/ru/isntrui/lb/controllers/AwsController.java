package ru.isntrui.lb.controllers;

import cn.hutool.core.date.DateTime;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.isntrui.lb.services.AwsService;
import ru.isntrui.lb.services.UserService;
import org.apache.commons.io.FilenameUtils;
import java.io.InputStream;
import java.util.Objects;

@RestController
@RequestMapping("/api/aws/")
@Tag(name = "AWS")
public class AwsController {
    @Autowired
    private UserService us;

    @Autowired
    private AwsService aws;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(MultipartFile file, String type) {
        String n;
        String res;
        try {
            n = type + "_" + Objects.requireNonNull(file.getOriginalFilename()).hashCode() + "_" + us.getCurrentUser().getId() + "-" + DateTime.now().toString("yyyy-MM-dd_HH-mm-ss");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        try {
            InputStream is = file.getInputStream();
            res = aws.uploadFile(is, n, FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res);
    }
}
