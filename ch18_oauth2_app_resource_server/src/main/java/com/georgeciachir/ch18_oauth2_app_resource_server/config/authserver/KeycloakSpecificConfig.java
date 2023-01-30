package com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.List;

import static org.springframework.security.oauth2.jwt.JwtClaimNames.AUD;

@Configuration
public class KeycloakSpecificConfig {

    @Value("${claim.aud}")
    private String claimAudience;
    @Value("${keycloak.base.url}")
    private String keycloakBaseUrl;

    @Bean
    public JwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setPrincipalClaimName("user_name");
        authConverter.setJwtGrantedAuthoritiesConverter(keycloakJwtGrantedAuthoritiesConverter());
        return authConverter;
    }

    @Bean
    public JwtGrantedAuthoritiesConverter keycloakJwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        return authoritiesConverter;
    }

    @Bean
    public JwtDecoder keycloakJwtDecoder() {
        OAuth2TokenValidator<Jwt> audienceValidator = new DelegatingOAuth2TokenValidator<>(audienceValidator());
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(keycloakBaseUrl);
        jwtDecoder.setJwtValidator(audienceValidator);

        return jwtDecoder;
    }

    @Bean
    public OAuth2TokenValidator<Jwt> audienceValidator() {
        return new JwtClaimValidator<List<String>>(AUD, aud -> aud.contains(claimAudience));
    }
}
