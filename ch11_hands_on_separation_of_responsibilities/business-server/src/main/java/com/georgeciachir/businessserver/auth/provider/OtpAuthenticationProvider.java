package com.georgeciachir.businessserver.auth.provider;

import com.georgeciachir.businessserver.auth.OtpAuthentication;
import com.georgeciachir.businessserver.auth.proxy.AuthorizationServerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthorizationServerProxy authServerProxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String code = String.valueOf(authentication.getCredentials());

        authServerProxy.validateOtp(username, code);

        return new OtpAuthentication(username, code);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class
                .isAssignableFrom(authentication);
    }
}
