package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Path;
import java.nio.file.Paths;

@Hidden
@Controller
@RequestMapping("/api/")
public class CoverageController {

    @GetMapping("coverage")
    public ModelAndView getCoverageReport() {
        try {
            Path filePath = Paths.get("build/reports/jacoco/test/html/index.html").toAbsolutePath();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setViewName("redirect:/api/coverage-report");
                return modelAndView;
            } else {
                return new ModelAndView("error/404");
            }
        } catch (Exception e) {
            return new ModelAndView("error/500");
        }
    }

    @GetMapping("coverage-report")
    public ResponseEntity<Resource> serveCoverageReport() {
        try {
            Path filePath = Paths.get("build/reports/jacoco/test/html/index.html").toAbsolutePath();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"coverage-report.html\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}