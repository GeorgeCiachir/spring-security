package com.georgeciachir.resource_server.config.authserver;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public record JwtDecodingData(JwtDecoder jwtDecoder,
                              JwtAuthenticationConverter jwtAuthenticationConverter,
                              String jwkUrl) {
}
