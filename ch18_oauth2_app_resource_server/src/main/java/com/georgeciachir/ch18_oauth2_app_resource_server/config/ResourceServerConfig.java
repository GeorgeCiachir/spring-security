package com.georgeciachir.ch18_oauth2_app_resource_server.config;

import com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver.AuthServerConfig;
import com.georgeciachir.ch18_oauth2_app_resource_server.config.authserver.TrustedIssuerJwtAuthenticationManagerResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class ResourceServerConfig {

    @Autowired
    private AuthServerConfig keycloakJwtConfig;

    @Autowired
    private AuthServerConfig auth0JwtConfig;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private TrustedIssuerJwtAuthenticationManagerResolver authenticationManagerResolver;

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
                    .oauth2ResourceServer()
                        // You can use either the authenticationManagerResolver or the jwt config
                        // The jwt config is not "additive" and does not accept multiple decoders and converters
                        // So, for the app to be able to decode JWTs from multiple issuers, the authenticationManagerResolver can be used
                        .authenticationManagerResolver(authenticationManagerResolver())
//                        .jwt()
//                            .jwkSetUri(auth0JwtConfig.jwkUrl())
//                            .decoder(auth0JwtConfig.jwtDecoder())
//                            .jwtAuthenticationConverter(auth0JwtConfig.jwtAuthenticationConverter())
//                            .jwkSetUri(keycloakJwtConfig.jwkUrl())
//                            .decoder(keycloakJwtConfig.jwtDecoder())
//                            .jwtAuthenticationConverter(keycloakJwtConfig.jwtAuthenticationConverter())
//                        .and()
                .and()
                .build();
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {
        return new JwtIssuerAuthenticationManagerResolver(authenticationManagerResolver);
    }

    // Required so that the expression used at the ProductRepository can be evaluated
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
