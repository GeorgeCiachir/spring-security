package com.georgeciachir.ch10_applying_csrf_and_cors.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping("/hello")
    public String getHello(@RequestParam(required = false, defaultValue = "world") String name) {
        return "Hello " + name + "!";
    }

    // if csrf(c -> c.ignoringRequestMatchers("/hello")); would not be configured in the SecurityConfigNew
    // this POST would not work unless it also comes with the X-CSRF-TOKEN request header. This header is the default value and
    // can be overridden
    @PostMapping("/hello")
    public String postHello(@RequestParam(required = false, defaultValue = "world") String name) {
        return "Hello " + name + "!";
    }
}
