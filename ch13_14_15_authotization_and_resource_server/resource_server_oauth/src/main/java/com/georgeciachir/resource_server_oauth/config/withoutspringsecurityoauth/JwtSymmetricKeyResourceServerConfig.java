package com.georgeciachir.resource_server_oauth.config.withoutspringsecurityoauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@ConditionalOnProperty(value = "configuration.mode", havingValue = "no-spring-security-oauth")
@Profile("jwt-symmetric-key-token-store")
@Configuration
public class JwtSymmetricKeyResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.symmetric.key}")
    private String jwtSymmetricKey;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(
                        serverConfigurer -> serverConfigurer.jwt(
                                jwtConfigurer -> jwtConfigurer
                                        .decoder(jwtDecoder())
                                        .jwtAuthenticationConverter(customAuthConverter())
                        ));
    }

    /**
     * Because this implementation does not rely on Oauth2 and is a just a jwt configuration, for a jwt that
     * can essentially contain anything, the framework couldn't now whatever custom value I use for the
     * username claim, so I must specify it.
     */
    private JwtAuthenticationConverter customAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName("user_name");
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] key = jwtSymmetricKey.getBytes();
        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        return NimbusJwtDecoder.withSecretKey(originalKey).build();
    }
}
