package com.georgeciachir.ch18_oauth2_app_resource_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class ResourceServerConfig {

    @Value("${security.keycloak.jwk.key.uri}")
    private String keycloakUrlJwk;

    @Autowired
    private JwtDecoder keycloakJwtDecoder;

    @Autowired
    private JwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    @Value("${security.auth0.jwk.key.uri}")
    private String auth0UrlJwk;

    @Autowired
    private JwtDecoder auth0JwtDecoder;

    @Autowired
    private JwtAuthenticationConverter auth0JwtAuthenticationConverter;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                    .oauth2Login()
                    .clientRegistrationRepository(clientRegistrationRepository)
                .and()
                    .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.DELETE, "/**").hasRole("imperial-admin")
                    .anyRequest().authenticated()
                .and()
                    // will replace this with a custom JwtIssuerAuthenticationManagerResolver, based on the token issuer
                    // and will extract this into the KeycloakResourceServerConfig
                    .oauth2ResourceServer()
                        .jwt()
                        .jwkSetUri(auth0UrlJwk)
                        .decoder(auth0JwtDecoder)
                        .jwtAuthenticationConverter(auth0JwtAuthenticationConverter)
//                        .jwkSetUri(keycloakUrlJwk)
//                        .decoder(keycloakJwtDecoder)
//                        .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
                    .and()
                .and()
                .build();
    }

    // Required so that the expression used at the ProductRepository can be evaluated
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
