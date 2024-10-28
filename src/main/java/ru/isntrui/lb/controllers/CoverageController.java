package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Path;
import java.nio.file.Paths;

@Hidden
@Controller
@RequestMapping("/api/coverage/")
public class CoverageController {

    @GetMapping("report")
    public ResponseEntity<Resource> serveCoverageReport() {
        try {
            Path filePath = Paths.get("build/reports/jacoco/test/html/index.html").toAbsolutePath();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"coverage-report.html\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("jacoco-resources/{res}")
    public ResponseEntity<Resource> getRes(@PathVariable String res) {
        return getResource("build/reports/jacoco/test/html/jacoco-resources/" + res);
    }

    @GetMapping("{dir}/{res}")
    public ResponseEntity<Resource> getRep(@PathVariable String dir, @PathVariable String res) {
        return getResource("build/reports/jacoco/test/html/" + dir + "/" + res);
    }

    private ResponseEntity<Resource> getResource(String resourcePath) {
        try {
            Path filePath = Paths.get(resourcePath).toAbsolutePath();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}