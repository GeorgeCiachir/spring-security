package com.georgeciachir.ch4_password_encoder.controller;

import com.georgeciachir.ch4_password_encoder.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @GetMapping("/encode")
    public String hello(@RequestParam String password, @RequestParam(required = false) String encoding) {
        return passwordService.encode(password, encoding);
    }
}
