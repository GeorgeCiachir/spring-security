package com.georgeciachir.businessserver.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserNamePasswordAuthentication extends UsernamePasswordAuthenticationToken {

    public UserNamePasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
