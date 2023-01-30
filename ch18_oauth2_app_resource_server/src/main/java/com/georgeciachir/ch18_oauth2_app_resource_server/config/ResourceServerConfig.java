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
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.oauth2.jwt.JwtClaimNames.AUD;

@EnableMethodSecurity
@Configuration
public class ResourceServerConfig {

    @Value("${claim.aud}")
    private String claimAudience;

    @Value("${keycloak.base.url}")
    private String keycloakBaseUrl;

    @Value("${jwk.key-uri}")
    private String urlJwk;

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
                    .oauth2ResourceServer()
                        .jwt()
                        .jwkSetUri(urlJwk)
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(myJwtAuthenticationConverter())
                    .and()
                .and()
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> audienceValidator = new DelegatingOAuth2TokenValidator<>(audienceValidator());
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(keycloakBaseUrl);
        jwtDecoder.setJwtValidator(audienceValidator);

        return jwtDecoder;
    }

    @Bean
    public OAuth2TokenValidator<Jwt> audienceValidator() {
        return new JwtClaimValidator<List<String>>(AUD, aud -> aud.contains(claimAudience));
    }

    // Required so that the expression used at the ProductRepository can be evaluated
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public JwtAuthenticationConverter myJwtAuthenticationConverter() {
        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setPrincipalClaimName("user_name");
        authConverter.setJwtGrantedAuthoritiesConverter(myJwtGrantedAuthoritiesConverter());
        return authConverter;
    }

    @Bean
    public JwtGrantedAuthoritiesConverter myJwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        return authoritiesConverter;
    }
}
