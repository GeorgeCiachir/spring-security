package com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public record AuthServerConfig(JwtDecoder jwtDecoder,
                               JwtAuthenticationConverter jwtAuthenticationConverter,
                               String issuerUrl,
                               String jwkUrl) {
}
