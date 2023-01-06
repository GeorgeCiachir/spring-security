package com.georgeciachir.ch7_configuring_authorization_restricting_access.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {


    @GetMapping("/hello")
    public String hello(@RequestParam(required = false, defaultValue = "world") String name) {
        return "Hello " + name + "!";
    }
}
