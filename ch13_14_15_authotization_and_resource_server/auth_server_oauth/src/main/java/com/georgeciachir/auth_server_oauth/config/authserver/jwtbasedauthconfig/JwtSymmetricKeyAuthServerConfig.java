package com.georgeciachir.auth_server_oauth.config.authserver.jwtbasedauthconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Profile("jwt-symmetric-key-token-store")
@Configuration
public class JwtSymmetricKeyAuthServerConfig extends JwtBasedAuthServerConfig {

    @Value("${jwt.symmetric.key}")
    private String jwtSymmetricKey;

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtSymmetricKey);
        return converter;
    }
}
