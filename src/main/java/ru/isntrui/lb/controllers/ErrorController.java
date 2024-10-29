package ru.isntrui.lb.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final String ERROR_PATH = "/error";

    private final ErrorAttributes errorAttributes;

    @Autowired
    public ErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(path = ERROR_PATH, method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(
                new ServletWebRequest(request),
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STATUS)
        );
        int statusCode = (int) errorDetails.getOrDefault("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        String htmlResponse = "<!DOCTYPE HTML>" +
                "<html>" +
                "<head><title>Error " + statusCode + "</title></head>" +
                "<body style=\"margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: black;\">" +
                "<img src='https://http.cat/" + statusCode + "' alt='Error " + statusCode + "' style='max-width: 100%; height: auto;' />" +
                "</body>" +
                "</html>";
        return ResponseEntity.status(statusCode)
                .header("Content-Type", "text/html")
                .body(htmlResponse);
    }
}
