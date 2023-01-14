package com.georgeciachir.resource_server_oauth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        Object toDisplay;
        if (authentication instanceof OAuth2Authentication) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            toDisplay = details.getDecodedDetails();
        } else {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            toDisplay = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(toList());
        }

        return "Hello " + toDisplay;
    }
}
