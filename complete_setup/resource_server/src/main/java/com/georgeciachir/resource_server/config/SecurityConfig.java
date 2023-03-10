package com.georgeciachir.resource_server.config;

import com.georgeciachir.resource_server.config.authserver.JwtConfig;
import com.georgeciachir.resource_server.config.authserver.TrustedIssuerJwtAuthenticationManagerResolver;
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
public class SecurityConfig {

    @Autowired
    private JwtConfig keycloakJwtConfig;

    @Autowired
    private JwtConfig auth0JwtConfig;

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
//                            .jwkSetUri(auth0JwtConfig.getJwtDecodingData().jwkUrl())
//                            .decoder(auth0JwtConfig.getJwtDecodingData().jwtDecoder())
//                            .jwtAuthenticationConverter(auth0JwtConfig.getJwtDecodingData().jwtAuthenticationConverter())
//                            .jwkSetUri(keycloakJwtConfig.getJwtDecodingData().jwkUrl())
//                            .decoder(keycloakJwtConfig.getJwtDecodingData().jwtDecoder())
//                            .jwtAuthenticationConverter(keycloakJwtConfig.getJwtDecodingData().jwtAuthenticationConverter())
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
