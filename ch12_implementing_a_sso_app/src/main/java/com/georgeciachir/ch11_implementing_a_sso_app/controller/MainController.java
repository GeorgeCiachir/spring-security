package com.georgeciachir.ch11_implementing_a_sso_app.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping("/hello")
    public String main(OAuth2AuthenticationToken token) {
        String user = String.valueOf(token.getPrincipal());
        return "Hello!" + user;
    }
}
