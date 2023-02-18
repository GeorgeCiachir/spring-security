package com.georgeciachir.resource_server.config.authserver;

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
public class SpringOauth2JwtConfig implements JwtConfig {

    @Value("${security.spring.oauth2.base.url}")
    private String springOAuth2BaseUrl;

    @Value("${security.spring.oauth2.jwk.key.uri}")
    private String springOAuth2UrlJwk;

    @Autowired
    private OAuth2TokenValidator<Jwt> audienceValidator;

    @Override
    public JwtDecodingData getJwtDecodingData() {
        return new JwtDecodingData(springOAuth2JwtDecoder(), springOAuth2JwtAuthenticationConverter(), springOAuth2UrlJwk);
    }

    @Override
    public String getIssuerUrl() {
        return springOAuth2BaseUrl;
    }

    public JwtAuthenticationConverter springOAuth2JwtAuthenticationConverter() {
        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setPrincipalClaimName("sub");
        authConverter.setJwtGrantedAuthoritiesConverter(auth0JwtGrantedAuthoritiesConverter());
        return authConverter;
    }

    public JwtGrantedAuthoritiesConverter auth0JwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("permissions");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        return authoritiesConverter;
    }

    public JwtDecoder springOAuth2JwtDecoder() {
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(audienceValidator);
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(springOAuth2BaseUrl);
        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }
}
