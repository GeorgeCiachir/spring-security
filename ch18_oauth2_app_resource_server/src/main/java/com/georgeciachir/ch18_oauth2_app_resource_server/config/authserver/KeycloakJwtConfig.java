package com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class KeycloakJwtConfig implements JwtConfig {

    @Value("${security.keycloak.base.url}")
    private String keycloakBaseUrl;

    @Value("${security.keycloak.jwk.key.uri}")
    private String keycloakUrlJwk;

    @Autowired
    private OAuth2TokenValidator<Jwt> audienceValidator;

    public JwtDecodingData getJwtDecodingData() {
        return new JwtDecodingData(keycloakJwtDecoder(), keycloakJwtAuthenticationConverter(), keycloakUrlJwk);
    }

    public String getIssuerUrl() {
        return keycloakBaseUrl;
    }

    public JwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setPrincipalClaimName("user_name");
        authConverter.setJwtGrantedAuthoritiesConverter(keycloakJwtGrantedAuthoritiesConverter());
        return authConverter;
    }

    public JwtGrantedAuthoritiesConverter keycloakJwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        return authoritiesConverter;
    }

    public JwtDecoder keycloakJwtDecoder() {
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(audienceValidator);
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(keycloakBaseUrl);
        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }
}
