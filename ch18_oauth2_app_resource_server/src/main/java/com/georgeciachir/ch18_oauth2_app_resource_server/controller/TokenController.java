package com.georgeciachir.ch18_oauth2_app_resource_server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping
    public String main(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username;
        String message;

        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            username = String.valueOf(jwt.getClaims().get("user_name"));
            message = "The token is: " + jwt.getTokenValue();
        } else {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            username = oAuth2User.getAttribute("user_name");
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            message = "No token used. Session id is: " + details.getSessionId();
        }

        return createHtml(username, message);
    }

    private String createHtml(String user, String message) {
        return "<p>Hello " + user + "!</p>" +
                "<p>" + message + "</p>";
    }
}
