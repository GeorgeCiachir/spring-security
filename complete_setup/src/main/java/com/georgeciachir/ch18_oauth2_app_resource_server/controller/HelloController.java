package com.georgeciachir.ch18_oauth2_app_resource_server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String main(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = authentication.getName();
        String message;

        if (principal instanceof Jwt jwt) {
            message = "The token is: " + jwt.getTokenValue();
        } else if (principal instanceof DefaultOAuth2User) {
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            message = "No token used. Session id is: " + details.getSessionId();
        } else {
            username = "Could not extract username info based on the used authentication type";
            message = "Could not create message based on the used authentication type";
        }

        return createHtml(username, message);
    }

    private String createHtml(String user, String message) {
        return "<p>Hello " + user + "!</p>" +
                "<p>" + message + "</p>";
    }
}
