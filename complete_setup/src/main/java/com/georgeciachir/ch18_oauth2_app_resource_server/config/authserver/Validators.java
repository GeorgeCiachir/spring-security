package com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;

import java.util.List;

import static org.springframework.security.oauth2.jwt.JwtClaimNames.AUD;

@Configuration
public class Validators {

    @Value("${claim.aud}")
    private String claimAudience;

    @Bean
    public OAuth2TokenValidator<Jwt> audienceValidator() {
        return new JwtClaimValidator<List<String>>(AUD, aud -> aud.contains(claimAudience));
    }
}
