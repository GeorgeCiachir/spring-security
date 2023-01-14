package com.georgeciachir.resource_server_oauth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        String name;
        // Same thing both cases -> just needed to point out the different Authentication types
        if (authentication instanceof OAuth2Authentication) {
            // If the server configuration is done with the configuration.mode=with-spring-security-oauth setting
            name = authentication.getName();
        } else {
            // If the server configuration is done with the configuration.mode=no-spring-security-oauth setting
            name = authentication.getName();
        }

        return "Hello " + name + "!";
    }
}
