package com.georgeciachir.ch10_applying_csrf_and_cors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class TestCorsController {

    private static final Logger LOG = Logger.getLogger(MainController.class.getName());

    @GetMapping("/cors")
    public String main() {
        return "cors.html";
    }

    @PostMapping("/test-cors")
    @ResponseBody
    // This server's domain is http://127.0.0.1:8080. Even though localhost is resolved as 127.0.0.1
    // the CORS policy checks for the actual strings (127.0.0.1 vs localhost)

    // We can add the annotation here, only for this method,
    // or we could configure CORS globally in the SecurityConfigNew class
    @CrossOrigin("http://localhost:8080")
    public String test() {
        LOG.info("Test method called");
        return "Hello! CORS is enabled";
    }
}
