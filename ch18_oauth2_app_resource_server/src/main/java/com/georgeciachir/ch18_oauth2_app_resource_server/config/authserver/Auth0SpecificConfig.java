package com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class Auth0SpecificConfig {

    @Value("${security.auth0.base.url}")
    private String auth0BaseUrl;

    @Value("${security.auth0.jwk.key.uri}")
    private String auth0UrlJwk;

    @Autowired
    private OAuth2TokenValidator<Jwt> audienceValidator;

    @Bean
    public AuthServerConfig auth0JwtConfig() {
        return new AuthServerConfig(auth0JwtDecoder(), auth0JwtAuthenticationConverter(), auth0BaseUrl, auth0UrlJwk);
    }

    public JwtAuthenticationConverter auth0JwtAuthenticationConverter() {
        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setPrincipalClaimName("user_name");
        authConverter.setJwtGrantedAuthoritiesConverter(auth0JwtGrantedAuthoritiesConverter());
        return authConverter;
    }

    public JwtGrantedAuthoritiesConverter auth0JwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("permissions");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        return authoritiesConverter;
    }

    public JwtDecoder auth0JwtDecoder() {
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(audienceValidator);
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(auth0BaseUrl);
        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }
}
