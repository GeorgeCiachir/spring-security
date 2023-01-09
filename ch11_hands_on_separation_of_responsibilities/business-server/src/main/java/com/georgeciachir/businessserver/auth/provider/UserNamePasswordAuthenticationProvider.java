package com.georgeciachir.businessserver.auth.provider;

import com.georgeciachir.businessserver.auth.UserNamePasswordAuthentication;
import com.georgeciachir.businessserver.auth.proxy.AuthorizationServerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UserNamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthorizationServerProxy authServerProxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        authServerProxy.validateCredentials(username, password);

        return new UserNamePasswordAuthentication(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserNamePasswordAuthentication.class
                .isAssignableFrom(authentication);
    }
}
