package com.georgeciachir.ch11_implementing_a_sso_app.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class MainController {

    private static final Logger LOG = Logger.getLogger(MainController.class.getName());

    @GetMapping("/")
    public String main(OAuth2AuthenticationToken token) {
        String user = String.valueOf(token.getPrincipal());
        LOG.info(user);
        return "main.html";
    }
}
