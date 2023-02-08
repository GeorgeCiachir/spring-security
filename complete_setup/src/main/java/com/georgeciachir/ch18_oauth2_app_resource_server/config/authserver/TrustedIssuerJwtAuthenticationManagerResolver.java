package com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class TrustedIssuerJwtAuthenticationManagerResolver implements AuthenticationManagerResolver<String> {

    private final List<String> trustedIssuers;
    private final Map<String, JwtConfig> authConfigs;
    private final Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();

    public TrustedIssuerJwtAuthenticationManagerResolver(@Value("${security.trusted.issuers}") List<String> trustedIssuers,
                                                         KeycloakJwtConfig keycloakJwtConfig,
                                                         Auth0JwtConfig auth0JwtConfig) {
        this.trustedIssuers = trustedIssuers;
        authConfigs = Map.of(
                keycloakJwtConfig.getIssuerUrl(), keycloakJwtConfig,
                auth0JwtConfig.getIssuerUrl(), auth0JwtConfig);
    }

    @Override
    public AuthenticationManager resolve(String issuer) {
        if (!trustedIssuers.contains(issuer)) {
            log.info("Did not resolve AuthenticationManager since issuer is not trusted");
            return null;
        }

        AuthenticationManager authenticationManager = authenticationManagers.computeIfAbsent(issuer, this::contructAuthenticationManager);
        log.info("Resolved AuthenticationManager for issuer {}", issuer);
        return authenticationManager;
    }

    private AuthenticationManager contructAuthenticationManager(String issuer) {
        log.info("Constructing AuthenticationManager for: {}", issuer);
        JwtDecodingData jwtDecodingData = authConfigs.get(issuer).getJwtDecodingData();
        JwtDecoder decoder = jwtDecodingData.jwtDecoder();
        JwtAuthenticationConverter converter = jwtDecodingData.jwtAuthenticationConverter();

        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(decoder);
        authenticationProvider.setJwtAuthenticationConverter(converter);
        return authenticationProvider::authenticate;
    }
}