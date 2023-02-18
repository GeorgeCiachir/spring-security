package com.georgeciachir.resource_server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public HelloDto main(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = authentication.getName();
        String message;

        if (principal instanceof Jwt jwt) {
            Map<String, Object> claims = jwt.getClaims();
            String issuer = (String) claims.get("iss");
            return createGreeting(username, issuer, jwt.getTokenValue(), claims);
        } else if (principal instanceof DefaultOAuth2User) {
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            message = "No token used. Session id is: " + details.getSessionId();
        } else {
            message = "Could not create message based on the used authentication type";
        }

        return new HelloDto(message, null, null, null);
    }

    private HelloDto createGreeting(String username, String issuer, String access_token, Map<String, Object> claims) {
        String message = String.format("Hello, %s ! This is the message from the resource server", username);
        return new HelloDto(message, issuer, access_token, claims);
    }
}
