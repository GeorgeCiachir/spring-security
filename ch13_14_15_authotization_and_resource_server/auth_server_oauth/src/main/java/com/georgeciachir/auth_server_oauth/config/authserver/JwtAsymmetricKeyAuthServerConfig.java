package com.georgeciachir.auth_server_oauth.config.authserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Profile("jwt-asymmetric-key-token-store")
@Configuration
public class JwtAsymmetricKeyAuthServerConfig extends JwtBasedAuthServerConfig {

    @Value("${private.key}")
    private String privateKey;

    @Value("${key.store.password}")
    private String keyStorePassword;

    @Value("${key.alias}")
    private String alias;

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        ClassPathResource classPathResource = new ClassPathResource(privateKey);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keyStorePassword.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias);

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);

        return converter;
    }
}
